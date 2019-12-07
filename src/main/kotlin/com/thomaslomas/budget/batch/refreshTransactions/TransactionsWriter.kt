package com.thomaslomas.budget.batch.refreshTransactions

import com.thomaslomas.budget.persistance.models.Transaction
import com.thomaslomas.budget.persistance.repositories.TransactionRepository
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired

class TransactionsWriter : ItemWriter<MutableList<Transaction>> {
    @Autowired
    lateinit var transactionRepository: TransactionRepository;

    override fun write(items: MutableList<out MutableList<Transaction>>) {
        for (item in items) {
            for (transaction in item) {
                transactionRepository.save(transaction)
            }
        }
    }
}