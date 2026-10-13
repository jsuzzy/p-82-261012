package com.back

class WiseSayingService(
    val wiseSayingMemRepository: WiseSayingMemRepository = SingletonScope.wiseSayingMemRepository
) {

    fun add(content: String, author: String): WiseSaying = WiseSaying(content, author).also{
            wiseSayingMemRepository.save(it)
        }

    fun findListDesc(): List<WiseSaying> = wiseSayingMemRepository.findListDesc()

    fun findById(id: Int): WiseSaying? = wiseSayingMemRepository.findById(id)

    fun delete(wiseSaying: WiseSaying) = wiseSayingMemRepository.delete(wiseSaying)

}