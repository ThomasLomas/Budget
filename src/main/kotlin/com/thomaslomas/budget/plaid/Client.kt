package com.thomaslomas.budget.plaid

import com.plaid.client.PlaidClient
import com.plaid.client.request.AccountsBalanceGetRequest
import com.plaid.client.request.ItemPublicTokenExchangeRequest
import com.plaid.client.request.TransactionsGetRequest
import com.plaid.client.response.Account
import com.plaid.client.response.TransactionsGetResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class Client(
    @Value("\${plaid.clientId}") val clientId: String,
    @Value("\${plaid.secret}") val secret: String,
    @Value("\${plaid.publicKey}") val publicKey: String,
    @Value("\${plaid.environment}") val environment: String
) {
    fun getClient(): PlaidClient {
        val builder = PlaidClient.newBuilder()
            .clientIdAndSecret(clientId, secret)
            .publicKey(publicKey)

        return if (environment == "sandbox") {
            builder.sandboxBaseUrl().build()
        } else {
            builder.productionBaseUrl().build()
        }
    }

    fun getAccessToken(publicToken: String): String {
        val response = getClient().service()
                .itemPublicTokenExchange(ItemPublicTokenExchangeRequest(publicToken)).execute()

        if(!response.isSuccessful) {
            throw Exception("Could not get access token: ${response.errorBody()}")
        }

        return response.body().accessToken
    }

    fun getBalances(accessToken: String): MutableList<Account> {
        val request = AccountsBalanceGetRequest(accessToken);
        val response = getClient().service().accountsBalanceGet(request).execute();

        if (!response.isSuccessful) {
            throw Exception("Could not get balances: ${response.errorBody()}")
        }

        return response.body().accounts
    }

    fun getTransactions(accessToken: String, startDate: Date, endDate: Date): MutableList<TransactionsGetResponse.Transaction> {
        val request = TransactionsGetRequest(accessToken, startDate, endDate)
        val response = getClient().service().transactionsGet(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Could not get transactions: ${response.errorBody()}")
        }

        return response.body().transactions
    }
}