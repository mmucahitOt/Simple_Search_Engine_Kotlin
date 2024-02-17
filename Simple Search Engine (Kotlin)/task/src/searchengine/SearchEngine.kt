package searchEngine

import java.io.File
import java.util.*



class SearchEngine(path: String) {
    private val people: MutableList<Person> = emptyList<Person>().toMutableList()
    private var word = "empty"
    private var peopleFound = mutableListOf<Person>()
    private var engineOn = true
    val invertedWords = mutableMapOf<String, MutableList<Int>>()
    private val filePath = path
    private var matchingStrategy = "ALL"
    init {
        //addPeople()
        addPeopleFromFile(filePath)
        invertWords()
    }

    fun invertWords() {
        val uniqueWords = mutableSetOf<String>()
        for (person in people) {
            person.personalInformation.lowercase().split(" ","  ", "   ", "    ", "     ").forEach { word-> uniqueWords.add(word)}
        }

        for (uniqueWord in uniqueWords) {
            invertedWords[uniqueWord] = emptyList<Int>().toMutableList()
        }
        for (uniqueWord in uniqueWords) {
            for (person in people) {
                for (word in person.personalInformation.lowercase().split(" "))
                    if (uniqueWord == word.lowercase()) {
                        invertedWords[uniqueWord]?.add(people.indexOf(person))
                    }
            }
        }

    }
    fun addPeopleFromFile(path : String) {
        val file = File(path)
        for (personalInformation in file.readLines()) {
            people.add(Person(personalInformation))
        }
    }

    fun askSearchStrategy() {
        println("Select a matching strategy: ALL, ANY, NONE")
        matchingStrategy = readLine()!!
        println()
    }

    fun searchOn(){
        while (engineOn) {
            printCommands()
            var userCommand = readLine()!!
            println()
            if (!Commands.possibleCommandCodes.contains(userCommand)) {
                println()
                println("Incorrect option! Try again.")
                println()
                continue
            }
            val userCommandCode = userCommand.toInt()
            when (userCommandCode){
                0 -> {
                    exit()
                    break
                }
                1 -> {
                    findAperson()
                }
                2 -> {
                    printAllPeople()
                }
            }
        }
    }

    fun printCommands() {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
    }

    fun findAperson() {
        askSearchStrategy()
        println("Enter a name or email to search all suitable people.")
        val userInput = readLine()!!.lowercase(Locale.getDefault())

        //search()
        //searchInvertedIndices()
        searchByStrategy(userInput)
        if (peopleFound.size == 0) {
            println("No matching people found.")
        } else {
            for (person in peopleFound) {
                println(person.personalInformation)
            }
            println()
        }
        peopleFound.clear()
    }

    fun printAllPeople() {
        println("=== List of people ===")
        for (person in people) {
            println(person.personalInformation)
        }
        println()
    }

    fun exit() {
        println("Bye!")
    }

    private fun addPeople() {
        println("Enter the number of people:")
        val numberOfPeople = readLine()!!.toInt()
        println("Enter all people:")
        for (i in 0 until numberOfPeople) {
            addPersonFromInput()
        }
        println()
    }


    private fun searchInvertedIndices(word: String) {
        for (index in invertedWords[word]?: emptyList<Int>().toMutableList()) {
            val foundPerson = people[index]
            peopleFound.add(foundPerson)
        }
    }

    private fun searchByStrategy(userInput: String) {
        val words = userInput.lowercase().split(" ")
        var allIndices = mutableSetOf<Int>()
        var indices = setOf<Int>()
        var overlappingIndices = mutableSetOf<Int>()
        for (word in words) {
            for (index in invertedWords[word]?: mutableListOf<Int>()) {
                overlappingIndices.add(index)
            }
        }

        when (matchingStrategy) {
            "ALL" -> {
                var intersectingIndices = invertedWords[words[0]]?:mutableListOf<Int>().toSet()
                for (word in words) {
                    intersectingIndices = intersectingIndices.intersect((invertedWords[word]?:mutableListOf<Int>().toSet()).toSet())
                }
                indices = intersectingIndices.toMutableSet()
            }

            "ANY" -> {
                indices = overlappingIndices
            }

            "NONE" -> {

                val uniqueWordsIndices = invertedWords.values.toList()
                uniqueWordsIndices.forEach { it.forEach { allIndices.add(it) }  }
                indices = allIndices - overlappingIndices
            }

            else -> {
            }
        }

        for (index in indices) {
            val foundPerson = people[index]
            peopleFound.add(foundPerson)
        }
    }
    private fun search() {

        val personalInformations = emptyList<String>().toMutableList()
        for (person in people) {
            personalInformations.add(person.personalInformation.lowercase())
        }

        for (personalInformation in personalInformations) {
            if(word in personalInformation) {
                val index = personalInformations.indexOf(personalInformation)
                val personFound = people[index]
                peopleFound.add(personFound)
            }
        }
    }

    private fun addPersonFromInput() {
        val personalInformation = readLine()!!
        people.add(Person(personalInformation))
    }
}