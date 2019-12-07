package com.thomaslomas.budget.persistance.repositories

import com.thomaslomas.budget.persistance.models.Account
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AccountRepository : MongoRepository<Account, ObjectId> {
    fun findOneByAccountIdAndEnvironment(accountId: String, environment: String): Account;
}