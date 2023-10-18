package com.example.data

import java.util.*
import java.util.logging.Logger


object PropertyReader {

    private val LOGGER = Logger.getLogger(PropertyReader::class.java.name)
    private var properties = Properties()
    var isLoaded = false
        private set


    fun load() {
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"))
        isLoaded = true
    }

    fun getProperty(name: String): String? {
        return properties.getProperty(name)
    }
}