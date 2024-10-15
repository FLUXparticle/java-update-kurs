package com.example.web;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.*;

public class CocktailServer {

    private final Connection connection;

    public CocktailServer() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:Cocktails.db");
    }

    public static void main(String[] args) throws Exception {
        CocktailServer handler = new CocktailServer();

        // Erstellen des HttpServers
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(Executors.newCachedThreadPool());

        Filter outputFilter = new LoggingFilter();

        // Handler für den Pfad "/cocktails"
        server.createContext("/cocktails", exchange -> {
                    if ("GET".equals(exchange.getRequestMethod())) {
                        handler.handleCocktails(exchange);
                    } else {
                        handler.handleNotFound(exchange);
                    }
                })
                .getFilters().add(outputFilter);

        // Handler für den Pfad "/cocktails/{id}"
        server.createContext("/cocktails/", exchange -> {
                    if ("GET".equals(exchange.getRequestMethod())) {
                        handler.handleCocktailDetails(exchange);
                    } else {
                        handler.handleNotFound(exchange);
                    }
                })
                .getFilters().add(outputFilter);

        // Starten des Servers
        server.start();
        System.out.println("Server started...");
    }

    // Handler für den Pfad "/cocktails"
    private void handleCocktails(HttpExchange exchange) throws IOException {
        try {
            String query = "SELECT id, name FROM cocktail";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                // Ergebnis abrufen
                ResultSet resultSet = preparedStatement.executeQuery();

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);

                try (var out = new PrintStream(exchange.getResponseBody())) {
                    // Cocktails anzeigen
                    while (resultSet.next()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        out.println(id + " " + name);
                        out.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            respondWithText(exchange, 500, "Internal Server Error");
        }
    }

    // Handler für den Pfad "/cocktails/{id}"
    private void handleCocktailDetails(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String cocktailId = extractCocktailId(path);

        try {
            // SQL-Abfrage für Details zu einem bestimmten Cocktail
            String query = "SELECT i.amountcl, ig.name " +
                    "FROM instruction i " +
                    "JOIN ingredient ig ON i.ingredient_id = ig.id " +
                    "WHERE i.instructions_id = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, Integer.parseInt(cocktailId));
                ResultSet resultSet = preparedStatement.executeQuery();

                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, 0);

                try (var out = new PrintStream(exchange.getResponseBody())) {
                    // Informationen zu den Zutaten abrufen
                    while (resultSet.next()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        int amountcl = resultSet.getInt("amountcl");
                        String name = resultSet.getString("name");
                        out.println(amountcl + " " + name);
                        out.flush();
                    }
                }
            }

            // respondWithText(exchange, 200, result);
        } catch (Exception e) {
            e.printStackTrace();
            respondWithText(exchange, 500, "Internal Server Error");
        }
    }

    // Handler für den Fall, dass kein passender Handler gefunden wurde
    private void handleNotFound(HttpExchange exchange) throws IOException {
        respondWithText(exchange, 404, "Not Found");
    }

    // Hilfsmethode zum Senden von JSON-Antworten
    private static void respondWithText(HttpExchange exchange, int statusCode, String text) throws IOException {
        // Setzen der Header und Senden der Antwort
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, 0);
        try (var out = new PrintStream(exchange.getResponseBody())) {
            out.println(text);
        }
    }

    // Hilfsmethode zum Extrahieren der Cocktail-Id aus dem Pfad
    private static String extractCocktailId(String path) {
        return path.substring("/cocktails/".length());
    }

}
