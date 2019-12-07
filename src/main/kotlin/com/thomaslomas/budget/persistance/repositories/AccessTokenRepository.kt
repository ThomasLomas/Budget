package com.thomaslomas.budget.persistance.repositories

import com.thomaslomas.budget.persistance.models.AccessToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AccessTokenRepository : MongoRepository<AccessToken, ObjectId>