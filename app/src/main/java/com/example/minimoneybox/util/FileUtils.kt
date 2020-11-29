package com.example.minimoneybox.util

object FileUtils {
    fun readTestResourceFile(fileName: String, folderName: String): String {
        val fileInputStream = javaClass.classLoader?.getResourceAsStream(folderName+"/"+fileName)
        return fileInputStream?.bufferedReader()?.readText() ?: ""
    }
}
