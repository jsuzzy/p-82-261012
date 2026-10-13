package com.back

class WiseSayingController(
    val wiseSayingService: WiseSayingService = SingletonScope.wiseSayingService
) {

    fun write() {
        print("명언: ")
        val content = readln().trim()

        print("작가: ")
        val author = readln().trim()
        val wiseSaying = wiseSayingService.add(content, author)

        println("${wiseSaying.id}번 명언이 등록되었습니다.")
    }

    fun list(rq: Rq)  {

        val keywordType = rq.getParam("keywordType", "content")
        val keyword = rq.getParam("keyword", "")

        val wiseSayings = if (keyword.isNotBlank())
            wiseSayingService.findByKeyword(keywordType, keyword)
        else
            wiseSayingService.findListDesc()


        if (keyword.isNotBlank()) {
            println("----------------------")
            println("검색타입 : $keywordType")
            println("검색어 : $keyword")
            println("----------------------")
        }
        println("번호 / 작가 / 명언")
        println("----------------------")

        wiseSayings.forEach {
            println("${it.id} / ${it.author} / ${it.content}")
        }
    }

    fun delete(rq: Rq) {
        val id = rq.getParamAsInt("id", 0)

        if (id == 0) {
            println("id를 정확히 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)

        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        wiseSayingService.delete(wiseSaying)
        println("${id}번 명언이 삭제되었습니다.")
    }

    fun modify(rq: Rq) {

        val id = rq.getParamAsInt("id", 0)

        if (id == 0) {
            println("id를 정확히 입력해주세요.")
            return
        }

        val wiseSaying = wiseSayingService.findById(id)

        if (wiseSaying == null) {
            println("${id}번 명언은 존재하지 않습니다.")
            return
        }

        println("명언(기존) : ${wiseSaying.content}): ")
        print("명언 : ")
        val newContent = readln().trim()
        println("작가(기존: ${wiseSaying.author}): ")
        print("작가 : ")
        val newAuthor = readln().trim()

        wiseSaying.modify(newContent, newAuthor)
        println("${id}번 명언이 수정되었습니다.")
    }
}