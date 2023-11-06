package com.wanted.restaurant.util.openAPI;

import com.wanted.restaurant.util.openAPI.dto.RestaurantOpenAPIDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAPIReader {

    @Autowired
    private final OpenAPIJSONParser openAPIJSONParser;

    @Value("${open-api.url}")
    private String BASE_URL; // 경기도 공공데이터 포털 open api 호출 url

    @Value("${open-api.key}")
    private String API_KEY; // open api 호출 시 필요한 인증 키

    public List<RestaurantOpenAPIDto> readOpenAPIData(String endpoint) throws IOException, ParseException {
        log.info("================ start reading OpenAPI ================");
        log.info("  - endpoint : " + endpoint);

        // 1. URL을 만들기 위한 StringBuffer
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + endpoint); /*URL*/
        // 2. 오픈 API의요청 규격에 맞는 파라미터 생성, 발급받은 인증키.
        urlBuilder.append("?" + URLEncoder.encode("KEY","UTF-8") + "=" + API_KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("Type","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*XML 또는 JSON*/

        // 3. URL 객체 생성.
        URL url = new URL(urlBuilder.toString());
        // 4. 요청하고자 하는 URL과 통신하기 위한 Connection 객체 생성.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 5. 통신을 위한 메소드 SET.
        conn.setRequestMethod("GET");
        // 6. 통신을 위한 Content-type SET.
        conn.setRequestProperty("Content-type", "application/json");
        // 7. 통신 응답 코드 확인.
        System.out.println("Response code: " + conn.getResponseCode());
        // 8. 전달받은 데이터를 BufferedReader 객체로 저장.
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        // 9. 저장된 데이터를 라인별로 읽어 StringBuilder 객체로 저장.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        // 10. 객체 해제.
        rd.close();
        conn.disconnect();
        // 11. 전달받은 데이터 확인.
        log.info("OpenAPI Data [String] :: " + sb.toString());

        return openAPIJSONParser.dataParsing(sb.toString(), endpoint);
    }
}
