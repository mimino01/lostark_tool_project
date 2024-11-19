package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetLostarkData {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String LOSTARK_API_KEY = dotenv.get("TOKEN");
    private static final String URL = "https://developer-lostark.game.onstove.com/markets/items";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final List<List<Map<String, Object>>> items = new ArrayList<>();

    public GetLostarkData() {}
    public static List<List<Map<String, Object>>> getData(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            try {
                List<Map<String, Object>> result = fetchData(data);
                items.add(result);
            } catch (Exception e) {
                System.out.println("오류 발생" + e.getMessage());
            }
        }
        return items;
    }

    private static List<Map<String, Object>> fetchData(Map<String, Object> jsonData) throws Exception {
        String jsonBody = mapper.writeValueAsString(jsonData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept", "application/json")
                .header("Authorization", LOSTARK_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP 오류 발생: " + response.statusCode());
        }

        Map<String, Object> responseJson = mapper.readValue(response.body(), Map.class);
        return (List<Map<String, Object>>) responseJson.getOrDefault("Items", new ArrayList<>());
    }
}
