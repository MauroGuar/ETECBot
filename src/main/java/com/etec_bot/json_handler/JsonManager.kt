package com.etec_bot.json_handler

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

class JsonManager {
    init {
    }

    private fun getJsonFiles(): ArrayList<File> {
        val fileList: ArrayList<File> = arrayListOf()
        val folder = File("src/main/resources")
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

    private fun getJsonStr(): ArrayList<String>? {
        var jsonStrList: ArrayList<String>? = arrayListOf()
        val jsonFiles = getJsonFiles()
        if (jsonFiles.isNotEmpty()) {
            jsonFiles.forEach {
                jsonStrList?.add(it.readText())
            }
        } else {
            jsonStrList = null
        }
        return jsonStrList
    }


    /*private fun <T> convertJsonsToObjectList(): List<T>? {
       val jsonMapper = jacksonObjectMapper()
       val typeReference: TypeReference<List<T>> = object : TypeReference<List<T>>() {}
        val jsonsContent = getJsonStr()
        var objectsList: List<T>? = null
        jsonsContent?.forEach {
           objectsList = jsonMapper.readValue(it, typeReference)
        }
        return objectsList
    }*/
    /*private fun convertJsonsToTestList(): List<Test>? {
        val jsonMapper = jacksonObjectMapper()
        val typeReference: TypeReference<List<Test>> = object : TypeReference<List<Test>>() {}
        val jsonsContent = getJsonStr()
        var objectsList: List<Test>? = null
        jsonsContent?.forEach {
            try {
                objectsList = jsonMapper.readValue(it, typeReference)
            } catch (e: MismatchedInputException) {
            }
        }
        return objectsList
    }*/
}
