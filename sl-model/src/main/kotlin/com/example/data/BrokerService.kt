package com.example.data

import com.example.model.IBrokerService
import com.example.model.entity.format
import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class BrokerService : IBrokerService {

    private val property = PropertyReader
    private val factory = ConnectionFactory()

    private val QUEUE_INSERT_NAME = "insert"
    private val QUEUE_UPDATE_NAME = "update"

    private val insertList = AccumulatedData("insert")
    private val updateList = AccumulatedData("update")

    private var isDeclared = false

    init {
        factory.host = property.getProperty("broker.host")
        factory.port = property.getProperty("broker.port")?.toInt()!!
    }

    override fun accumulateData(name: String, date: String, status: String) {
        insertList.date = date
        updateList.date = date

        when (status[0]) {
            'i' -> insertList.list.add(name)
            'u' -> updateList.list.add(name)
        }
    }

    override fun send() {
        factory.newConnection().use { connection ->
            connection.createChannel().use { insertChannel ->
                if (!isDeclared) {
                    insertChannel.queueDeclare(QUEUE_INSERT_NAME, false, false, false, null)
                }
                insertChannel.basicPublish("", QUEUE_INSERT_NAME, null, convertToByte(insertList))
            }

            connection.createChannel().use { updateChannel ->
                if (!isDeclared) {
                    updateChannel.queueDeclare(QUEUE_UPDATE_NAME, false, false, false, null)
                    isDeclared = true
                }
                updateChannel.basicPublish("", QUEUE_UPDATE_NAME, null, convertToByte(updateList))
            }
        }

        with(insertList) {
            date = ""
            list.clear()
        }

        with(updateList) {
            date = ""
            list.clear()
        }
    }

    private fun convertToByte(data: AccumulatedData): ByteArray {
        return format.encodeToString(data).toByteArray(charset = charset("UTF-8"))
    }
}

@Serializable
data class AccumulatedData(val status: String, var date: String = "", var list: MutableList<String> = mutableListOf())