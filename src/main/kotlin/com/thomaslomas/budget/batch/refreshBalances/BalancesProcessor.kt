package com.thomaslomas.budget.batch.refreshBalances

import com.thomaslomas.budget.persistance.models.AccessToken
import com.thomaslomas.budget.persistance.models.Account
import com.thomaslomas.budget.persistance.models.Balance
import com.thomaslomas.budget.persistance.repositories.AccountRepository
import com.thomaslomas.budget.plaid.Client
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.dao.EmptyResultDataAccessException

@Configuration
class BalancesProcessor() : ItemProcessor<AccessToken, MutableList<Account>> {
    @Autowired
    lateinit var plaidClient: Client;

    @Value("\${plaid.environment}")
    lateinit var environment: String;

    @Autowired
    lateinit var accountRepository: AccountRepository;

    override fun process(item: AccessToken): MutableList<Account> {
        val accounts = mutableListOf<Account>()

        for (balance in plaidClient.getBalances(item.accessToken)) {
            var mongoAcct = Account()

            try {
                mongoAcct = accountRepository.findOneByAccountIdAndEnvironment(
                        accountId = balance.accountId,
                        environment = environment
                )
            }  catch (ex: EmptyResultDataAccessException) {
                // Record is new
            }

            mongoAcct.accountId = balance.accountId
            mongoAcct.accessToken = item._id
            mongoAcct.environment = environment
            mongoAcct.name = balance.name
            mongoAcct.officialName = balance.officialName
            mongoAcct.subType = balance.subtype
            mongoAcct.type = balance.type
            mongoAcct.balance = Balance(
                    available = balance.balances.available,
                    currencyCode = balance.balances.isoCurrencyCode,
                    current = balance.balances.current
            )

            accounts.add(mongoAcct)
        }

        return accounts
    }
}