package com.example.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

public class HttpUtils {

    // Methode, die einen GET-Request an die gegebene URL ausführt und die Antwort als Stream von Zeilen zurückgibt
    public static Stream<String> getResponseLines(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return in.lines(); // Stream der Zeilen zurückgeben
    }

    public static void main(String[] args) throws Exception {
        try (Stream<String> lines = HttpUtils.getResponseLines("http://localhost:8080/cocktails")) {
            lines.forEach(line -> {
                // Format ist: ID Name
                String[] parts = line.split(" ");
                String id = parts[0];
                String name = parts[1];

                System.out.println(id + " " + name);
            });
        }
    }

}