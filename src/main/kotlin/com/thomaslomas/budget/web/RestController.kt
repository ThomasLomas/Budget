package com.thomaslomas.budget.web

import com.thomaslomas.budget.persistance.models.AccessToken
import com.thomaslomas.budget.persistance.models.Account
import com.thomaslomas.budget.persistance.models.Balance
import com.thomaslomas.budget.persistance.repositories.AccessTokenRepository
import com.thomaslomas.budget.persistance.repositories.AccountRepository
import com.thomaslomas.budget.plaid.Client
import com.thomaslomas.budget.web.models.AccessTokenRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RestController (
        val plaidClient: Client,
        val accessTokenRepository: AccessTokenRepository,
        val accountRepository: AccountRepository,
        @Value("\${plaid.environment}") val environment: String) {
    private val logger = LoggerFactory.getLogger(javaClass);

    @PostMapping("/get_access_token", consumes = ["application/json"], produces = ["application/json"])
    fun getAccessTokenRequest(@RequestBody request: AccessTokenRequest): Boolean {
        logger.info("Fetching access token with ${request.publicToken}")

        var accessToken = plaidClient.getAccessToken(request.publicToken)
        logger.info("Fetched access token: $accessToken")

        accessTokenRepository.insert(AccessToken(accessToken = accessToken, environment = environment))

        return true;
    }
}