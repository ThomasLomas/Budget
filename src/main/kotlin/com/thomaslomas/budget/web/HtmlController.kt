package com.thomaslomas.budget.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HtmlController(
        @Value("\${plaid.publicKey}") val publicKey: String,
        @Value("\${plaid.environment}") val environment: String,
        @Value("\${plaid.clientName}") val clientName: String
) {
    @GetMapping("/")
    fun setup(model: Model): String {
        model["title"] = "Budget"
        model["publicKey"] = publicKey
        model["environment"] = environment
        model["clientName"] = clientName
        return "setup"
    }
}