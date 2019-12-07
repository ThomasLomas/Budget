package com.thomaslomas.budget.persistance.repositories

import com.thomaslomas.budget.persistance.models.Transaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, ObjectId> {
    fun findOneByAccountIdAndTransactionIdAndEnvironment(accountId: String, transactionId: String, environment: String): Transaction;

}