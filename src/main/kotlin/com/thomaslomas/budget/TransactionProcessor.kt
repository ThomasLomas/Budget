package com.thomaslomas.budget

import com.plaid.client.response.TransactionsGetResponse
import org.springframework.batch.item.ItemProcessor

class TransactionProcessor : ItemProcessor<TransactionsGetResponse.Transaction, TransactionsGetResponse.Transaction> {
    override fun process(item: TransactionsGetResponse.Transaction): TransactionsGetResponse.Transaction? {
        return item
    }
}