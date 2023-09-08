package com.etec_bot.json_handler

import com.etec_bot.ETECBot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.ChannelType
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class JsonManager {
    init {
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


    inner class TodoListManager(bot: ETECBot) {
        private var actualTodoLists: ArrayList<TodoList> = arrayListOf()
        private val jsonPath = "src/main/resources/todo-lists.json"
        private val jsonDirectoryPath = "src/main/resources/"
        private var gson: Gson = Gson();
        private var jsonExist: Boolean = false
        private val api = bot.api

        init {
            jsonSetUp()
        }

        private fun jsonSetUp() {
            jsonExist = checkJsonExistOrEmpty()
            if (!jsonExist) {
                newJsonContent("todo-lists.json", jsonDirectoryPath)
            }
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
            File(pathToWrite).writeText(jsonContentToWrite)
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

        private fun updateJson(guildID: String, channelId: String) {

        }

        //      Functions related with the list management
        fun create(todoList: TodoList): Boolean {
            newJsonContent("todo-lists.json", jsonDirectoryPath)
            if (checkOnlyChannelList(todoList)) {
                actualTodoLists.add(todoList)
                updateJson()
                return true
            }
            return false
        }

        private fun checkOnlyChannelList(todoList: TodoList): Boolean {
            return actualTodoLists.find { it.channelId == todoList.channelId } == null
        }

        fun addTask(guildID: String, channelId: String, taskContent: String): Boolean {
            val tdList = actualTodoLists.find { it.guildID == guildID && it.channelId == channelId }
            val index = actualTodoLists.indexOf(tdList)
            val taskToAdd = Task(taskContent, null)
            if (tdList?.let { checkOnlyChannelList(it) } == false) {
                if (tdList.tasks == null) {
                    tdList.tasks = arrayListOf()
                }
                tdList.tasks!!.add(taskToAdd)
                actualTodoLists[index] = tdList
                updateJson()
                return true
            }
            return false
        }

        private fun updateJson() {
            writeJson(actualTodoLists, jsonPath)
        }

        
    }
}

inline fun <reified T> Gson.fromJsonArrayList(jsonStrContent: String) = fromJson<ArrayList<T>>(jsonStrContent,
    object : TypeToken<ArrayList<T>>() {})

inline fun <reified T> Gson.fromJsonList(jsonStrContent: String) = fromJson<List<T>>(jsonStrContent,
    object : TypeToken<List<T>>() {})


data class TodoList(
    val name: String,
    val channelId: String,
    val guildID: String,
    var tasks: ArrayList<Task>?,
    val ID: String = UUID.randomUUID().toString()
) {
}

data class Task(val content: String, val userTakenId: String?, val done: Boolean = false)
