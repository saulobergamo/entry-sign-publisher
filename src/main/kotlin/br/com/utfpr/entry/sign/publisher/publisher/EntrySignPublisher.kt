package br.com.utfpr.entry.sign.publisher.publisher

import br.com.utfpr.entry.sign.publisher.model.EntrySignMessage
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
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

    fun sendEntrySign(entrySignMessage: EntrySignMessage) {
        logger.info { "sendEntrySign: try to send entry sign to queue for user=${entrySignMessage.userName}" }

        val message = objectMapper.writeValueAsString(entrySignMessage)

        jmsTemplate.convertAndSend(queue, message).also {
            logger.info {
                "sendEntrySign: entry sign  sent to queue " +
                    "with success for user=${entrySignMessage.userName}"
            }
        }
    }
}
