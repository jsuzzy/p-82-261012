package com.back

import java.nio.file.Path

class WiseSayingFileRepository : WiseSayingRepository{
    var lastId = 0

    // 파일을 저장할 경로를 getter로 제공
    // 값을 실제로 저장하는 건 아니고 getter 제공이 마치 변수처럼 보이는 것
    val tableDirPath: Path
        get() {
            return AppConfig.dbDirPath.resolve("wiseSaying")
        }

    override fun save(wiseSaying: WiseSaying): WiseSaying {
        return wiseSaying
            .takeIf { it.isNew() }
            ?.also {
                wiseSaying.id = ++lastId
                saveOnDisk(wiseSaying)
            } ?: wiseSaying
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
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): WiseSaying? {
        TODO("Not yet implemented")
    }

    override fun delete(wiseSaying: WiseSaying): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}
