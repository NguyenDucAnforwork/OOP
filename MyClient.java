/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.scrapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class MyClient {

    public static void main(String[] args) {
        String query = "Sahara";
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:5000/similar_documents");

            // Add query data to the request
            StringEntity entity = new StringEntity("{\"query\": \"" + query + "\"}");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");

            // Execute the request and obtain the response
            HttpResponse response = client.execute(httpPost);
            
            if (response.getEntity() != null) {
                // Read the response content as a string
                String responseContent = EntityUtils.toString(response.getEntity());
                System.out.println("Response from server:");
                System.out.println(responseContent);
            } else {
                System.out.println("Empty response from server.");
            }
            
            // Process the response
            // Here you can parse the JSON response and use the data
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

