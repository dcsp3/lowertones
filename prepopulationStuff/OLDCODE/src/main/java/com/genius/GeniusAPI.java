package com.genius;

import com.datatypes.GeniusInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.utils.Json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class GeniusAPI {
    public GeniusInfo ingest(String songTitle, String artistName){
        GeniusInfo result = new GeniusInfo();
        //search for song, returns geniusID
        var client = HttpClient.newHttpClient();
        String ACCESS_TOKEN = "R2wHO6mCyiJ8gJhgY0LWQ3G-9WU4HiQVb1U_SvwfnDJuEwrZtBlaA6Aj-5Lkeq9K";
        try {
            String formattedSearchQuery = java.net.URLEncoder.encode(songTitle + " " + artistName, "UTF-8").replace("+", "%20");
            var searchRequest = HttpRequest.newBuilder(
                    URI.create("https://api.genius.com/search?q="+formattedSearchQuery))
                    .header("accept", "application/json")
                    .header("authorization", "bearer "+ACCESS_TOKEN)
                    .build();
            var searchResponse = client.send(searchRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode searchNode = Json.parse(searchResponse.body());
            if(searchNode.path("response").path("hits").size()==0){
                return result;
            } else {
                String geniusID = searchNode.path("response").path("hits").get(0).path("result").path("id").asText();
                var songRequest = HttpRequest.newBuilder(
                                URI.create("https://api.genius.com/songs/"+geniusID))
                        .header("accept", "application/json")
                        .header("authorization", "bearer "+ACCESS_TOKEN)
                        .build();
                var songResponse = client.send(songRequest, HttpResponse.BodyHandlers.ofString());
                JsonNode songNode = Json.parse(songResponse.body());

                String[] producersTemp = new String[songNode.path("response").path("song").path("producer_artists").size()];
                for (int j = 0; j < songNode.path("response").path("song").path("producer_artists").size(); j++) {
                    producersTemp[j] = songNode.path("response").path("song").path("producer_artists").get(j).path("name").asText();
                }
                String[] writersTemp = new String[songNode.path("response").path("song").path("writer_artists").size()];
                for (int j = 0; j < songNode.path("response").path("song").path("writer_artists").size(); j++) {
                    writersTemp[j] = songNode.path("response").path("song").path("writer_artists").get(j).path("name").asText();
                }
                result.setGeniusID(Integer.parseInt(geniusID));
                result.setProducersName(producersTemp);
                result.setWritersName(writersTemp);
                return result;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
