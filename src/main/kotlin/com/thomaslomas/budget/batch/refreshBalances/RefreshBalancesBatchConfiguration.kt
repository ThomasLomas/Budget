package com.thomaslomas.budget.batch.refreshBalances

import com.thomaslomas.budget.batch.BatchMongo
import com.thomaslomas.budget.persistance.models.AccessToken
import com.thomaslomas.budget.persistance.models.Account
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value

@Configuration
@EnableBatchProcessing
class RefreshBalancesBatchConfiguration (
        val jobBuilderFactory: JobBuilderFactory,
        val stepBuilderFactory: StepBuilderFactory,
        val batchMongo: BatchMongo,
        @Value("\${plaid.environment}") val environment: String
) {
    @Bean
    fun updateMongoAccounts() = stepBuilderFactory.get("updateMongoAccounts")
            .chunk<AccessToken, MutableList<Account>>(10)
            .reader(batchMongo.fetchMongoAccessKeysForBalance())
            .processor(createMongoAccounts())
            .writer(insertMongoAccounts())
            .build()

    @Bean
    fun createMongoAccounts(): ItemProcessor<AccessToken, MutableList<Account>> {
        return BalancesProcessor();
    }

    @Bean
    fun insertMongoAccounts(): ItemWriter<MutableList<Account>> {
        return BalancesWriter();
    }

    @Bean
    fun refreshBalancesJob() = jobBuilderFactory.get("refreshBalances")
            .start(updateMongoAccounts())
            .build()
}



