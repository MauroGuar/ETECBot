package com.etec_bot.json_handler

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

open class JsonManager {
    init {
//        val testManager = TestManager()
        val todoList = TodoListManager()
//        todoList.create(TodoList(UUID.randomUUID().toString(), "Test List", "1118350313167003678", "698250813180477460", arrayListOf(Task("Hacer la tareaaaa!", "506118589917429771"))))
    }

    private fun getJsonFiles(jsonsPath: String): ArrayList<File> {
        val fileList: ArrayList<File> = arrayListOf()
        val folder = File(jsonsPath)
        if (folder.exists()) {
            val files = folder.listFiles()
            files?.forEach { file ->
                if (file.isFile) {
                    fileList.add(file)
                }
            } ?: println("\u001B[31m" + "No files to read")
        }
        return fileList
    }

    private fun getAllJsonStr(jsonsPath: String): ArrayList<String>? {
        var jsonStrList: ArrayList<String>? = arrayListOf()
        val jsonFiles = getJsonFiles(jsonsPath) // default: "src/main/resources"
        if (jsonFiles.isNotEmpty()) {
            jsonFiles.forEach {
                jsonStrList?.add(it.readText())
            }
        } else {
            jsonStrList = null
        }
        return jsonStrList
    }

    internal fun findJsonStr(fileName: String, jsonsPath: String): String? {
        var jsonStr: String? = null
        val jsonFiles = getJsonFiles(jsonsPath) // default: "src/main/resources"
        if (jsonFiles.isNotEmpty()) {
            jsonStr = jsonFiles.find {
                it.name == fileName
            }?.readText()
        }
        if (jsonStr == "") {
            jsonStr = null
        }
        return jsonStr
    }

    internal fun writeFile(filePath: String, jsonStr: String) {
        return File(filePath).writeText(jsonStr)
    }

    inner class TodoListManager() {
        private var actualTodoLists: ArrayList<TodoList> = arrayListOf()
        private val jsonPath = "src/main/resources/todo-lists.json"
        private val jsonDirectoryPath = "src/main/resources/"
        private var gson: Gson = Gson();
        private var jsonExist: Boolean = false

        init {
            jsonSetUp()
        }

        private fun jsonSetUp() {
            jsonExist = checkJsonExistOrEmpty()
            if (!jsonExist) {
                newJsonContent("todo-lists.json", jsonDirectoryPath)
            }
        }

        public fun create(todoList: TodoList) {
            newJsonContent("todo-lists.json", jsonDirectoryPath)
            actualTodoLists.add(todoList)
            writeJson(actualTodoLists, jsonPath)
        }

        private fun checkJsonExistOrEmpty(): Boolean {
            val jsonStr = findJsonStr("todo-lists.json", jsonDirectoryPath)
            if (jsonStr != null) {
                actualTodoLists = gson.fromJsonArrayList<TodoList>(jsonStr)
                return true
            }
            return false
        }

        private fun writeJson(objToWrite: TodoList?, pathToWrite: String) {
            val jsonContentToWrite = gson.toJson(objToWrite)
            val fileToWrite = File(pathToWrite).writeText(jsonContentToWrite)
        }

        private fun writeJson(listToWrite: ArrayList<TodoList>?, pathToWrite: String) {
            val jsonContentToWrite = gson.toJson(listToWrite)
            val fileToWrite = File(pathToWrite).writeText(jsonContentToWrite)
        }

        private fun newJsonContent(jsonFileName: String, jsonDirPath: String) {
            val jsonFile = File(jsonDirPath, jsonFileName)
            if (!jsonFile.exists()) {
                jsonFile.createNewFile()
            }
        }
    }
}

inline fun <reified T> Gson.fromJsonArrayList(jsonStrContent: String) = fromJson<ArrayList<T>>(jsonStrContent,
    object : TypeToken<ArrayList<T>>() {})
inline fun <reified T> Gson.fromJsonList(jsonStrContent: String) = fromJson<List<T>>(jsonStrContent,
    object : TypeToken<List<T>>() {})

data class Test(val name: String?, val number: Int?)

data class TodoList(val uuid: String, val name: String, val channelId: String, val guildID: String, val tasks: ArrayList<Task>)
data class Task(val content: String, val userTakenId: String, val done: Boolean = false)
