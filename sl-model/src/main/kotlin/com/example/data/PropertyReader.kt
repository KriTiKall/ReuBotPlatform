package com.example.data

import java.util.*
import java.util.logging.Logger


object PropertyReader {

    private val LOGGER = Logger.getLogger(PropertyReader::class.java.name)
    private var properties = Properties()

    init {
    }

    fun load() {
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"))
    }

    fun getProperty(name: String): String? {
        return properties.getProperty(name)
    }
}