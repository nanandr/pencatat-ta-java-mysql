package com.nandanarafiardika.pencatatbukuta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams){
        URL url;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream outputStream = connection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(getPostDataString(postDataParams));
            bufferedWriter.flush();
            bufferedWriter.close();

            outputStream.close();
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                stringBuilder = new StringBuilder();
                String response;
                while ((response = bufferedReader.readLine()) != null){
                    stringBuilder.append(response);
                }
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return stringBuilder.toString();
    }
    public String sendGetRequest(String requestURL){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            while ((response = bufferedReader.readLine()) != null){
                stringBuilder.append(response + "\n");
            }
        }
        catch (Exception exception){}
        return stringBuilder.toString();
    }

    public String sendGetRequestParam(String requestURL, String id){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(requestURL + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            while ((response = bufferedReader.readLine()) != null){
                stringBuilder.append(response + "\n");
            }
        }
        catch (Exception exception){}
        return stringBuilder.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first){
                first = false;
            }
            else {
                stringBuilder.append("&");
            }
            stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return  stringBuilder.toString();
    }
}
