package com.wanted.restaurant.boundedContext.discord;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.service.MemberService;
import com.wanted.restaurant.boundedContext.restaurant.dto.LunchDTO;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.service.RestaurantService;
import com.wanted.restaurant.util.Ut;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebHookService {

	private final MemberService memberService;
	private final RestaurantService restaurantService;

	// 경기도 안양시
	double ANYANGLATITUDE = 37.3897;
	double ANYANGLONGITUDE = 126.9533556;

	@Value("${discord.webhookURL}")
	private String url;

	@Scheduled(cron = "0 30 11 * * ?", zone = "Asia/Seoul")
	public void callEvent() {
		RsData<List<LunchDTO>> rsResult = restaurantService.getTop3(ANYANGLATITUDE, ANYANGLONGITUDE,
		1);

		List<LunchDTO> result = rsResult.getData();

		// 제일 밖에 녀석 embeds 밖 선언
		Map<String, Object> message = new HashMap<>();

		// 안에 들어갈 embeds 객체
		List<Object> embeds = new ArrayList<>();

		Map<String, Object> embedsContent = new HashMap<>();

		// fields 생성
		List<Map<String, Object>> fields = new ArrayList<>();

		if(rsResult.isFail()) {
			Map<String, Object> field = new HashMap<>();

			field.put("name", "조회 실패");
			field.put("value", rsResult.getMsg());
			fields.add(field);

			// embeds 내용에 fields 추가
			embedsContent.put("fields", fields);
			embedsContent.put("color", 14177041);
			embeds.add(embedsContent);
			// embeds 내용을 embeds라는 키로 전체 밖에 넣기
			message.put("embeds", embeds);
		}

		else {
			message.put("content", "점심시간 30분 전! 아래 식당을 추천드립니다!");

			embedsContent.put("color", 15258703);
			embedsContent.put("title", "오늘의 점메추!");

			Map<String, Set<LunchDTO>> tmp = new HashMap<>();

			// 중식 : A, B, C 형태 저장용 tmp

			for (LunchDTO lunchDTO : result) {
				Set<LunchDTO> typeResults = tmp.getOrDefault(lunchDTO.getType(), new HashSet<>());
				typeResults.add(lunchDTO);
				tmp.put(lunchDTO.getType(), typeResults);
			}

			for (Map.Entry<String, Set<LunchDTO>> typeResult : tmp.entrySet()) {
				Map<String, Object> field = new HashMap<>();
				field.put("name", typeResult.getKey());
				// Set<LunchDTO>를 각각의 문자열로 변환하고, 이들을 줄바꿈 문자로 연결하여 value에 할당
				String value = typeResult.getValue().stream()
					.sorted(Comparator.comparingDouble(LunchDTO::getGrade).reversed())
					.map(lunchDTO -> String.format("**- 식당 이름** %s \n **- 주소** %s \n **- 평점** %.2f \n",
						lunchDTO.getName(), lunchDTO.getAddress(), lunchDTO.getGrade()))
					.collect(Collectors.joining("\n"));
				field.put("value", value);
				field.put("inline", "true");
				fields.add(field);
			}

			embedsContent.put("fields", fields);
			// embeds 내용에 fields 추가
			embeds.add(embedsContent);
			// embeds 내용을 embeds라는 키로 전체 밖에 넣기
			message.put("embeds", embeds);
		}
		send(Ut.json.mapToJSONObject(message));
	}

	private void send(JSONObject object) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);
		restTemplate.postForObject(url, entity, String.class);
	}
}