package com.wanted.restaurant.util.openAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.util.openAPI.dto.RestaurantOpenAPIDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAPIJSONParser {

    private final ObjectMapper objectMapper;

    public List<RestaurantOpenAPIDto> dataParsing(String strData, String endpoint) throws ParseException {
        JSONObject jsonObject = parserStringToJSON(strData);

        // 식당 정보 추출
        JSONArray genre = (JSONArray) jsonObject.get(endpoint);
        JSONObject row = (JSONObject) genre.get(1);
        JSONArray restaurantJsonList = (JSONArray) row.get("row");

        List<RestaurantOpenAPIDto> restaurantOpenAPIDtoList = new ArrayList<>();

        // json -> entity 변환
        for (Object restaurantJson : restaurantJsonList) {
            JSONObject jsonProperties = (JSONObject) restaurantJson;
            try {
                RestaurantOpenAPIDto restaurantOpenAPIDto = objectMapper.readValue(jsonProperties.toJSONString(), RestaurantOpenAPIDto.class);
                restaurantOpenAPIDtoList.add(restaurantOpenAPIDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return restaurantOpenAPIDtoList;
    }

    private JSONObject parserStringToJSON(String strData) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(strData);
    }
}
