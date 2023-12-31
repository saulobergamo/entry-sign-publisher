package br.com.utfpr.entry.sign.publisher.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("entry_sign_to_rebuild_image")
class EntrySign(
    @Id
    var id: String? = null,
    @Indexed
    var imageId: String? = null,
    var userName: String? = null,
    var signType: Boolean? = false,
    var entrySignDouble: List<Double>? = null
)
