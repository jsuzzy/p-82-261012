package com.back

class WiseSayingRepository {

    var lastId = 0
    val wiseSayings = mutableListOf<WiseSaying>()

    fun clear(){
        wiseSayings.clear()
        lastId = 0
    }

    fun save(wiseSaying: WiseSaying): WiseSaying {

        return wiseSaying
            .takeIf { it.isNew() }
            ?.also {
                wiseSaying.id = ++lastId
                wiseSayings.add(wiseSaying)
            } ?: wiseSaying
    }

    fun findListDesc() = wiseSayings.toList().reversed()

    fun findById(id: Int): WiseSaying? = wiseSayings.firstOrNull { it.id == id }

    fun delete(wiseSaying: WiseSaying) = wiseSayings.remove(wiseSaying)

}