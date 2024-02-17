package search_engine

import searchEngine.SearchEngine
import java.io.File

fun main(args: Array<String>) {
    //val path = "C:\\Users\\mmuca\\IdeaProjects\\Simple Search Engine\\Simple Search Engine\\task\\src\\searchEngine\\" + args[1]
    val path = args[1]
    val mySearchEngine = SearchEngine(path)
    mySearchEngine.searchOn()
}
