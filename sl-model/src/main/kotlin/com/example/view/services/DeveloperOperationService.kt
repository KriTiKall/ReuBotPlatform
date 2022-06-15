package com.example.view.services

class DeveloperOperationService {

}

interface IDeveloperOperations {

    fun getGroupNames() : Array<String>
    fun getTeacherNames() : Array<String>
    fun getSchedules(date: String) : Array<String>
}