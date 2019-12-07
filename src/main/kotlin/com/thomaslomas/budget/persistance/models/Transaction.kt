package com.thomaslomas.budget.persistance.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate
import java.util.*

@Document(collection = "transactions")
data class Transaction(
        @Id var _id: ObjectId? = null,
        @CreatedDate val createdDate: Date? = null,
        @LastModifiedDate val modifiedDate: Date? = null,

        @Field var environment: String? = null,
        @Field var accessToken: ObjectId? = null,
        @Field var transactionId: String,
        @Field var accountId: String,
        @Field var category: List<String>? = null,
        @Field var categoryId: String? = null,
        @Field var transactionType: String,
        @Field var name: String,
        @Field var amount: Double,
        @Field var currencyCode: String,
        @Field var transactionDate: LocalDate,
        @Field var location: Location,
        @Field var paymentMeta: PaymentMeta,
        @Field var pending: Boolean,
        @Field var pendingTransactionId: String? = null
) {
}

data class Location(
        @Field var address: String? = null,
        @Field var city: String? = null,
        @Field var region: String? = null,
        @Field var postalCode: String? = null,
        @Field var country: String? = null,
        @Field var latitude: Double?,
        @Field var longitude: Double?
) {}

data class PaymentMeta(
        @Field var reference: String? = null,
        @Field var ppdId: String? = null,
        @Field var payee: String? = null
) {}