package com.example.data

import com.example.model.IBrokerService
import com.example.model.entity.format
import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class BrokerService : IBrokerService {

    private val property = PropertyReader
    private val factory = ConnectionFactory().apply {
        host = property.getProperty("broker.host")
        port = property.getProperty("broker.port")!!.toInt()
    }

    private val QUEUE_NAME = property.getProperty("broker.queue.name")!!

    private val insertList = AccumulatedData(Status.INSERT)
    private val updateList = AccumulatedData(Status.UPDATE)

    private var isDeclared = false


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
            connection.createChannel().use { channel ->
                if (!isDeclared) {
                    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
                }

                if (insertList.list.isNotEmpty())
                    channel.basicPublish("", QUEUE_NAME, null, convertToByte(insertList))
                if (updateList.list.isNotEmpty())
                    channel.basicPublish("", QUEUE_NAME, null, convertToByte(updateList))
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

enum class Status() {
    UPDATE,
    INSERT
}

@Serializable
data class AccumulatedData(val status: Status, var date: String = "", var list: MutableList<String> = mutableListOf())