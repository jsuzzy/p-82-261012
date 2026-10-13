package com.back

class WiseSayingService(
    val wiseSayingRepository: WiseSayingRepository = SingletonScope.wiseSayingFileRepository
) {

    fun add(content: String, author: String): WiseSaying = WiseSaying(content, author).also{
        wiseSayingRepository.save(it)
        }

    fun findByKeyword(keywordType: String, keyword: String): List<WiseSaying> {
        return when ( keywordType ) {
            "author" -> wiseSayingRepository.findByAuthorLike("%$keyword%")
            "content" -> wiseSayingRepository.findByContentLike("%$keyword%")
            else -> wiseSayingRepository.findByAuthorContent("%$keyword%")
        }
    }

    fun findListDesc(): List<WiseSaying> = wiseSayingRepository.findListDesc()

    fun findById(id: Int): WiseSaying? = wiseSayingRepository.findById(id)

    fun delete(wiseSaying: WiseSaying) = wiseSayingRepository.delete(wiseSaying)

}