package com.android.testapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpController {

    public static final String GET_USERS = "http://je.su/test";
    public static final String GET_BALANCE = "http://je.su/test?mode=showuser&id=";

    public static InputStream get(String url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();

        return connection.getInputStream();
    }

    public static String balanceLink(int id) {

        return GET_BALANCE + String.valueOf(id);
    }

}
