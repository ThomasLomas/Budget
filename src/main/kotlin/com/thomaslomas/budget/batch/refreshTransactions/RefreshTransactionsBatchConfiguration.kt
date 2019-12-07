package com.thomaslomas.budget.batch.refreshTransactions

import com.thomaslomas.budget.batch.BatchMongo
import com.thomaslomas.budget.persistance.models.AccessToken
import com.thomaslomas.budget.persistance.models.Transaction
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class RefreshTransactionsBatchConfiguration (
        val jobBuilderFactory: JobBuilderFactory,
        val stepBuilderFactory: StepBuilderFactory,
        val batchMongo: BatchMongo,
        @Value("\${plaid.environment}") val environment: String
){
    @Bean
    fun updateMongoTransactions() = stepBuilderFactory.get("updateMongoTransactions")
            .chunk<AccessToken, MutableList<Transaction>>(10)
            .reader(batchMongo.fetchMongoAccessKeysForTransactions())
            .processor(createMongoTransactions())
            .writer(insertMongoTransactions())
            .build()

    @Bean
    fun createMongoTransactions(): ItemProcessor<AccessToken, MutableList<Transaction>> {
        return TransactionsProcessor();
    }

    @Bean
    fun insertMongoTransactions(): ItemWriter<MutableList<Transaction>> {
        return TransactionsWriter();
    }


    @Bean
    fun refreshTransactionsJob() = jobBuilderFactory.get("refreshTransactions")
            .start(updateMongoTransactions())
            .build()
}