package me.t3sl4.hydraulic.Util.Gen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static me.t3sl4.hydraulic.Util.Gen.Util.BASE_URL;

public class OrderFetcher {
    private static final String API_URL = BASE_URL + "/api/orderNumbers";

    public static List<String> fetchOrderNumbers() throws IOException {
        List<String> orderNumbers = new ArrayList<>();

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String output;
        while ((output = br.readLine()) != null) {
            orderNumbers.add(output.trim());
        }

        conn.disconnect();

        return orderNumbers;
    }
}
