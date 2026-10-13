package com.back

import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository{

    val tableDirPath: Path
        get() {
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        return wiseSaying
            .takeIf { it.isNew() }
            ?.also {
                wiseSaying.id = genNextId()
                saveOnDisk(wiseSaying)
            } ?: wiseSaying
    }

    fun saveLastId(lastId: Int) {
        mkTableDirsIfNotExists()

        tableDirPath.resolve("lastId.txt")
            .toFile()
            .writeText(lastId.toString())
    }

    fun loadLastId(): Int {
        return kotlin.runCatching {
            tableDirPath.resolve("lastId.txt")
                .toFile()
                .readText()
                .toInt()
        }.getOrElse { 0 }
    }

    fun genNextId(): Int {
        return (loadLastId() + 1).also {
            saveLastId(it)
        }
    }

    private fun saveOnDisk(wiseSaying: WiseSaying) {
        mkTableDirsIfNotExists()

        val wiseSayingFilePath = tableDirPath.resolve("${wiseSaying.id}.json")
        wiseSayingFilePath.toFile().writeText(wiseSaying.jsonStr)
    }

    private fun mkTableDirsIfNotExists() {
        tableDirPath.toFile().run {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    override fun findListDesc(): List<WiseSaying> {
        return tableDirPath.toFile()
            .listFiles()
            ?.filter { it.name.endsWith(".json") }
            ?.map { it.readText() }
            ?.map(WiseSaying::fromJsonStr)
            .orEmpty()
    }

    override fun findById(id: Int): WiseSaying? {
        return tableDirPath
            .toFile()
            .listFiles()
            ?.find { it.name == "${id}.json" }
            ?.let { WiseSaying.fromJsonStr(it.readText()) }
    }

    private fun filterByKeyword(
        keyword: String,
        selector: (WiseSaying) -> String
    ): List<WiseSaying> {

        val pure = keyword.replace("%", "")
        val wiseSayings = findListDesc()

        if (pure.isBlank()) return wiseSayings

        return when {
            keyword.startsWith("%") && keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).contains(pure) }
            keyword.startsWith("%") ->
                wiseSayings.filter { selector(it).endsWith(pure) }
            keyword.endsWith("%") ->
                wiseSayings.filter { selector(it).startsWith(pure) }
            else ->
                wiseSayings.filter { selector(it) == pure }
        }
    }

    override fun findByAuthorLike(authorLike: String): List<WiseSaying> {
        return filterByKeyword(authorLike) {it.author}
    }

    override fun findByContentLike(contentLike: String): List<WiseSaying> {
        return filterByKeyword(contentLike) {it.content}
    }

    override fun findByAuthorContent(contentLike: String): List<WiseSaying> {
        return filterByKeyword(contentLike) { it.content }
    }

    override fun delete(wiseSaying: WiseSaying): Boolean {
        return tableDirPath
            .resolve("${wiseSaying.id}.json")
            .toFile()
            ?.takeIf { it.exists() }
            ?.delete() ?: false
    }

    override fun clear() {
        tableDirPath.toFile().deleteRecursively()
    }
}
