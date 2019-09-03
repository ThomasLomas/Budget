package com.thomaslomas.budget

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
class BatchConfiguration (val jobBuilderFactory: JobBuilderFactory, val stepBuilderFactory: StepBuilderFactory) {
    @Bean
    fun step1() = stepBuilderFactory.get("step1")
            .tasklet { contribution, chunkContext ->
                println("Step 1 complete")
                RepeatStatus.FINISHED
            }
            .build()

    @Bean
    fun job() = jobBuilderFactory.get("job1")
            .start(step1())
            .build()
}
