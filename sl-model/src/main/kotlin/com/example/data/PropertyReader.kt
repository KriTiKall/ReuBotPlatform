package com.example.data

import java.util.*
import java.util.logging.Logger


object PropertyReader {

    private val LOGGER = Logger.getLogger(PropertyReader::class.java.name)
    private var properties = Properties()
    private var isLoad = false


    fun load() {
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"))
        isLoad = true
    }

    fun getProperty(name: String): String? {
        if (!isLoad)
            load()
        return properties.getProperty(name)
    }
}