package br.com.utfpr.entry.sign.publisher.repository

import br.com.utfpr.entry.sign.publisher.model.entity.EntrySign
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
interface EntrySignRepository : MongoRepository<EntrySign, String> {
    fun findByImageId(imageId: String): EntrySign?
}
