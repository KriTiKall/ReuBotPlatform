package com.example.data

import java.sql.Connection
import java.sql.DriverManager

object ConnectionGetter {

    private val property = PropertyReader

    fun getConnection() : Connection {
        val location = property.getProperty("db.location")
        val userName = property.getProperty("db.user_name")
        val userPassword = property.getProperty("db.user_password")

        val url = "$location?user=$userName&password=$userPassword"

        return DriverManager.getConnection(url)
    }
}