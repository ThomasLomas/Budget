package com.thomaslomas.budget.batch.refreshTransactions

import com.thomaslomas.budget.persistance.models.AccessToken
import com.thomaslomas.budget.persistance.models.Location
import com.thomaslomas.budget.persistance.models.PaymentMeta
import com.thomaslomas.budget.persistance.models.Transaction
import com.thomaslomas.budget.persistance.repositories.AccessTokenRepository
import com.thomaslomas.budget.persistance.repositories.TransactionRepository
import com.thomaslomas.budget.plaid.Client
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.dao.EmptyResultDataAccessException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
class TransactionsProcessor() : ItemProcessor<AccessToken, MutableList<Transaction>> {
    @Autowired
    lateinit var plaidClient: Client;

    @Value("\${plaid.environment}")
    lateinit var environment: String;

    @Autowired
    lateinit var transactionRepository: TransactionRepository;

    @Autowired
    lateinit var accessTokenRepository: AccessTokenRepository;

    private val logger = LoggerFactory.getLogger(javaClass);

    override fun process(accessToken: AccessToken): MutableList<Transaction> {
        val mongoTransactions = mutableListOf<Transaction>()
        val sixMonthsAgo = Calendar.getInstance()
        sixMonthsAgo.add(Calendar.MONTH, -6)

        val fromDate = accessToken.lastTransactionFetch ?: sixMonthsAgo.time

        // Update the last transaction fetch to now
        accessToken.lastTransactionFetch = Date()
        accessTokenRepository.save(accessToken)

        val transactions = plaidClient.getTransactions(accessToken.accessToken, fromDate, Date())

        logger.info("Found total of ${transactions.count()} transaction(s)")

        for (transaction in transactions) {
             try {
                 transactionRepository.findOneByAccountIdAndTransactionIdAndEnvironment(
                         accountId = transaction.accountId,
                         transactionId = transaction.transactionId,
                         environment = environment
                 )
                logger.info("Transaction ${transaction.transactionId} already exists")
             } catch (ex: EmptyResultDataAccessException) {
                logger.info("Transaction ${transaction.transactionId} does not exist - creating new")

                 mongoTransactions.add(Transaction(
                         environment = environment,
                         accessToken = accessToken._id,
                         transactionId = transaction.transactionId,
                         accountId = transaction.accountId,
                         category = transaction.category,
                         categoryId = transaction.categoryId,
                         transactionType = transaction.transactionType,
                         name = transaction.name,
                         amount = transaction.amount,
                         currencyCode = transaction.isoCurrencyCode,
                         transactionDate = LocalDate.parse(transaction.date, DateTimeFormatter.ISO_DATE),
                         location = Location(
                                 address = transaction.location.address,
                                 city = transaction.location.city,
                                 region = transaction.location.region,
                                 postalCode = transaction.location.postalCode,
                                 country = transaction.location.country,
                                 latitude = transaction.location.lat,
                                 longitude = transaction.location.lon
                         ),
                         paymentMeta = PaymentMeta(
                                 reference = transaction.paymentMeta.referenceNumber,
                                 ppdId = transaction.paymentMeta.ppdId,
                                 payee = transaction.paymentMeta.payee
                         ),
                         pending = transaction.pending,
                         pendingTransactionId = transaction.pendingTransactionId
                 ))
             }
        }

        return mongoTransactions
    }
}