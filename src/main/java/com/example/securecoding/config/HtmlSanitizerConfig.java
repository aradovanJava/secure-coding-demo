package com.example.securecoding.config;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguracija za HTML sanitizaciju korištenjem OWASP Java HTML Sanitizer
 * 
 * Ova klasa definira pravila za čišćenje HTML sadržaja od potencijalno
 * opasnih elemenata i atributa koji bi mogli biti korišteni za XSS napad.
 */
@Configuration
public class HtmlSanitizerConfig {

    /**
     * Policy factory koji dozvoljava samo osnovne HTML elemente
     * i sigurne atribute. Ovaj pristup slijedi "whitelist" princip
     * gdje se eksplicitno definiraju dozvoljeni elementi.
     */
    @Bean
    public PolicyFactory htmlSanitizer() {
        return new HtmlPolicyBuilder()
                // Dozvoljeni osnovni tekstualni elementi
                .allowElements("p", "br", "strong", "em", "b", "i", "u")
                // Dozvoljeni elementi za liste
                .allowElements("ul", "ol", "li")
                // Dozvoljeni naslovi
                .allowElements("h1", "h2", "h3", "h4", "h5", "h6")
                // Dozvoljeni linkovi - ALI samo sa sigurnim protokolima
                .allowElements("a")
                .allowAttributes("href").onElements("a")
                .allowStandardUrlProtocols()  // http, https, mailto
                // SVE OSTALO JE BLOKIRANO - uključujući <script>, onclick, itd.
                .toFactory();
    }
}
