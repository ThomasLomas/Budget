package com.thomaslomas.budget.persistance.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.Date

@Document(collection = "accounts")
data class Account(
        @Id var _id: ObjectId? = null,
        @CreatedDate val createdDate: Date? = null,
        @LastModifiedDate val modifiedDate: Date? = null,

        @Field var accessToken: ObjectId? = null,
        @Field var accountId: String? = null,
        @Field var environment: String? = null,
        @Field var name: String? = null,
        @Field var officialName: String? = null,
        @Field var subType: String? = null,
        @Field var type: String? = null,
        @Field var balance: Balance? = null
) {
}

data class Balance(
        @Field val available: Double?,
        @Field val current: Double?,
        @Field val currencyCode: String?
)