package com.thomaslomas.budget

import org.springframework.batch.core.Job
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.JobParameters

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class, DataSourceTransactionManagerAutoConfiguration::class, HibernateJpaAutoConfiguration::class])
@EnableScheduling
class BudgetApplication (val jobLauncher: JobLauncher, val job: Job) {
	@Scheduled(cron = "0 */1 * * * ?")
	fun perform() {
		val params = JobParametersBuilder()
				.addString("JobID", System.currentTimeMillis().toString())
				.toJobParameters()
		jobLauncher.run(job, params)
	}
}

fun main(args: Array<String>) {
	runApplication<BudgetApplication>(*args)
}