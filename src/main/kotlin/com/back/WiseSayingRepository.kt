package com.back

interface WiseSayingRepository {
    fun save(wiseSaying: WiseSaying): WiseSaying
    fun findListDesc(): List<WiseSaying>
    fun findById(id: Int): WiseSaying?
    fun findByAuthorLike(keyword: String): List<WiseSaying>
    fun findByContentLike(keyword: String): List<WiseSaying>
    fun findByAuthorContent(keyword: String): List<WiseSaying>
    fun delete(wiseSaying: WiseSaying): Boolean
    fun clear()
}