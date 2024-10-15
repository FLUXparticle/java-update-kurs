package com.example.web;

import com.sun.net.httpserver.*;
import org.slf4j.*;

import java.io.*;

class LoggingFilter extends Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        // Protokollieren der Anfrageinformationen
        logRequest(exchange);
        // Weiter zur nächsten Filter-Chain oder Handler
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Logs request information using SLF4J";
    }

    // Hilfsfunktion zum Protokollieren der Anfrage
    private void logRequest(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String uri = exchange.getRequestURI().toString();
        String remoteAddress = exchange.getRemoteAddress().toString();
        logger.info("Incoming Request: Method = {}, URI = {}, Remote Address = {}", method, uri, remoteAddress);
    }

}
