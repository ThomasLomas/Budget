package com.thomaslomas.budget

import com.thomaslomas.budget.batch.refreshBalances.RefreshBalancesBatchConfiguration
import com.thomaslomas.budget.batch.refreshTransactions.RefreshTransactionsBatchConfiguration
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication(exclude = [
	DataSourceAutoConfiguration::class,
	DataSourceTransactionManagerAutoConfiguration::class,
	HibernateJpaAutoConfiguration::class
])
@EnableScheduling
@EnableMongoRepositories
@EnableMongoAuditing
class BudgetApplication (
		val jobLauncher: JobLauncher,
		val refreshBalancesJob: RefreshBalancesBatchConfiguration,
		val refreshTransactionsJob: RefreshTransactionsBatchConfiguration
) {
	@Scheduled(cron = "0 */1 * * * ?")
	fun refreshBalances() {
		val params = JobParametersBuilder()
				.addString("JobID", System.currentTimeMillis().toString())
				.toJobParameters()
		jobLauncher.run(refreshBalancesJob.refreshBalancesJob(), params)
	}

	@Scheduled(cron = "0 */1 * * * ?")
	fun refreshTransactions() {
		val params = JobParametersBuilder()
				.addString("JobID", System.currentTimeMillis().toString())
				.toJobParameters()
		jobLauncher.run(refreshTransactionsJob.refreshTransactionsJob(), params)
	}
}

fun main(args: Array<String>) {
	runApplication<BudgetApplication>(*args)
}