package com.thomaslomas.budget.batch

import com.thomaslomas.budget.persistance.models.AccessToken
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import java.util.HashMap

@Configuration
class BatchMongo(val mongoTemplate: MongoTemplate) {
    @Bean
    fun fetchMongoAccessKeysForBalance(): MongoItemReader<AccessToken> {
        val reader = MongoItemReader<AccessToken>()
        reader.setTemplate(mongoTemplate)
        reader.setTargetType(AccessToken::class.java)
        reader.setQuery("{}");

        reader.setSort(object : HashMap<String, Sort.Direction>() {
            init {
                put("_id", Sort.Direction.DESC)
            }
        })

        return reader;
    }
    @Bean
    fun fetchMongoAccessKeysForTransactions(): MongoItemReader<AccessToken> {
        val reader = MongoItemReader<AccessToken>()
        reader.setTemplate(mongoTemplate)
        reader.setTargetType(AccessToken::class.java)
        reader.setQuery("{}");

        reader.setSort(object : HashMap<String, Sort.Direction>() {
            init {
                put("_id", Sort.Direction.DESC)
            }
        })

        return reader;
    }
}