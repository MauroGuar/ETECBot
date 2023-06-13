package com.etec_bot.json_handler

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

open class JsonManager {
    init {
        /*val gson = Gson()
        val tests2: List<Test> = listOf(Test("Test 3", 2124), Test("Test 4", 2323))
        val test3 = Test("Test 3", 4857)
        val json = gson.toJson(tests2)
        val file = File("src/main/resources/test.json")
        file.writeText(json)*/
        val testManager = TestManager()
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

    inner class TestManager {
        private var tests: List<Test>? = mutableListOf()
        private val jsonPath = "src/main/resources/test.json"
        private val jsonDirectoryPath = "src/main/resources/"
        private var gson: Gson = Gson();
        private var jsonExist: Boolean = false

        init {
            jsonExist = checkJsonExistOrEmpty()
            if (!jsonExist) {
                newJsonContent("test.json", jsonDirectoryPath)
                writeJson(tests, jsonPath)
            }
            println(tests)
        }

        private fun checkJsonExistOrEmpty(): Boolean {
            val jsonStr = findJsonStr("test.json", jsonDirectoryPath)
            if (jsonStr != null) {
                tests = gson.fromJsonList<Test>(jsonStr)
                return true
            }
            return false
        }

        private fun writeJson(listToWrite: List<Test>?, pathToWrite: String) {
            val jsonContentToWrite = gson.toJson(listToWrite)
            val fileToWrite = File(pathToWrite).writeText(jsonContentToWrite)
        }

        private fun writeJson(objToWrite: Test?, pathToWrite: String) {
            val jsonContentToWrite = gson.toJson(objToWrite)
            val fileToWrite = File(pathToWrite).writeText(jsonContentToWrite)
        }

        private fun newJsonContent(jsonFileName: String, jsonDirPath: String) {
            val jsonFile = File(jsonDirPath, jsonFileName)
            if (!jsonFile.exists()) {
                jsonFile.createNewFile()
            }
            tests = mutableListOf(Test("Test 1", 9988))
        }
    }

}

inline fun <reified T> Gson.fromJsonList(jsonStrContent: String) = fromJson<List<T>>(jsonStrContent,
    object : TypeToken<List<T>>() {})

data class Test(val name: String?, val number: Int?)