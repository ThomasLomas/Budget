package com.thomaslomas.budget.batch.refreshBalances

import com.thomaslomas.budget.persistance.models.Account
import com.thomaslomas.budget.persistance.repositories.AccountRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired

class BalancesWriter : ItemWriter<MutableList<Account>> {
    @Autowired
    lateinit var accountRepository: AccountRepository;

    override fun write(items: MutableList<out MutableList<Account>>) {
        for(item in items) {
            for(account in item) {
                accountRepository.save(account)
            }
        }
    }
}