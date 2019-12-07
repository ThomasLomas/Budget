package com.thomaslomas.budget.persistance.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.Date

@Document(collection = "access_tokens")
data class AccessToken(
        @Id val _id: ObjectId? = null,
        @CreatedDate val createdDate: Date? = null,
        @LastModifiedDate val modifiedDate: Date? = null,

        @Field val accessToken: String,
        @Field val environment: String,
        @Field var lastTransactionFetch: Date? = null
) {
}