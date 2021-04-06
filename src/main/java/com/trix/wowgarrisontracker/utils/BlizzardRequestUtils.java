package com.trix.wowgarrisontracker.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.trix.wowgarrisontracker.deserializers.AuctionHouseResponseDeserializer;
import com.trix.wowgarrisontracker.model.AuctionEntity;
import com.trix.wowgarrisontracker.model.Auctions;
import com.trix.wowgarrisontracker.model.Server;
import org.springframework.stereotype.Component;

public class BlizzardRequestUtils {


    public Auctions getAuctionHouse() {

        ObjectMapper auctionMapper = new ObjectMapper();
        String jsonData = "";
        String[] token = this.getToken().split(" ");
        ObjectMapper mapper = new ObjectMapper();
        Auctions ah = new Auctions();
        try {
            jsonData = getJsonData("curl -X GET https://eu.api.blizzard.com/data/wow/connected-realm/3713/auctions?"
                    + "namespace=dynamic-eu&locale=en_US&access_token=" + token[1]);
            SimpleModule module = new SimpleModule("AuctionHouseResponseDeserializer",
                    new Version(1, 0, 0, null, null, null));
            module.addDeserializer(AuctionEntity.class, new AuctionHouseResponseDeserializer());
            auctionMapper.registerModule(module);
            auctionMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            ah = auctionMapper.readValue(jsonData, Auctions.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ah;

    }

    public String getJsonData(String curlRequest) {
        String jsonData = "";
        try {
            Process process = Runtime.getRuntime().exec(curlRequest);
            InputStream inputStream = process.getInputStream();
            jsonData = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonData;
    }

    public String getToken() {
        String token = "";
        try {
            String jsonData = this.getJsonData(
                    "curl -u b0ea73a8d8f74e9b949e5c9868911b7f:C6LomejtnBU682JAdzOKMpZ7t5htKwvL -d grant_type=client_credentials https://us.battle.net/oauth/token");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Map<String, Object> mappedString = mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
            });
            token = mappedString.get("token_type") + " " + mappedString.get("access_token");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return token;
    }

    public List<Server> getListOfServers() {
        ObjectMapper mapper = new ObjectMapper();
        String[] token = this.getToken().split(" ");
        String json = "";
        List<String> regions = Arrays.asList("us", "eu");
        List<Server> servers = new ArrayList<>();
        try {
            for (String region : regions) {

                json = getJsonData("curl -X GET https://" + region.toUpperCase() + ".api.blizzard.com/data/wow/realm/index?namespace=dynamic-" + region + "&locale=en_US&access_token=" + token[1]);
                JsonNode parent = mapper.readTree(json);
                JsonNode realms = parent.path("realms");
                servers.addAll(Arrays.stream(mapper.convertValue(realms, Server[].class)).
                        peek(server -> server.setRegion(region))
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return servers;
    }

}
