package br.com.utfpr.entry.sign.publisher.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.jblas.DoubleMatrix
import org.springframework.beans.factory.annotation.Value
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class EntrySignPublisher(
    private val jmsTemplate: JmsTemplate,
    private val objectMapper: ObjectMapper,

    @Value("\${spring.activemq.queue.entry-sign}")
    private val queue: String
) {
    private val logger = KotlinLogging.logger {}

    fun sendEntrySign(entrySignString: DoubleMatrix?, documentNumber: String) {
        logger.info { "sendEntrySign: try to send entry sign to queue for user=$documentNumber" }
        val entrySignAsList = entrySignString?.elementsAsList()
        val message = objectMapper.writeValueAsString(documentNumber + entrySignAsList)

        jmsTemplate.convertAndSend(queue, message).also {
            logger.info { "sendEntrySign: entry sign  sent to queue with success for user=$documentNumber" }
        }
    }
}
