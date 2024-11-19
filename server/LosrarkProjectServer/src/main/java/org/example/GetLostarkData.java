package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public class GetLostarkData {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String LOSTARK_API_KEY = dotenv.get("TOKEN");

    public static void main(String[] args) {
        System.out.println("Lost Ark API Key: " + LOSTARK_API_KEY);
    }
}
