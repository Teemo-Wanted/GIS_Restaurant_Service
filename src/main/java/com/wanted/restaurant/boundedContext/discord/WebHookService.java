package com.wanted.restaurant.boundedContext.discord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.service.MemberService;
import com.wanted.restaurant.boundedContext.restaurant.dto.RestaurantResponse;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.service.RestaurantService;
import com.wanted.restaurant.util.Ut;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebHookService {

	private final MemberService memberService;
	private final RestaurantService restaurantService;

	// 경기도 고양시
	double GYEONGGIDOLATITUDE = 37.65590833;
	double GYEONGGIDOLONGITUDE = 126.7770556;

	@Value("${discord.webhookURL}")
	private String url;

	public void callEvent() {
		// 1. 전체 회원 중 알람 설정한 회원을 가져옴
		List<Member> members = memberService.getAllMembersByAlarmYes();
		// 2. 해당 회원들의 위, 경도을 추출하고 / DB에 저장된 식당 위치와 거리 비교
		// 회원 한명당 위치 정보를 추출하고 추천해야 하지만 경기도 고양시로 통일
		// 3. 타입별로 5개씩 식당 목록화 해서 반환이나 타입 검색은 구현하지 않았으므로 5개만 호출
		// TODO : 검색 메서드 수정 시 불러오기
		// RestaurantResponse.RestaurantList result = restaurantService.search(GYEONGGIDOLATITUDE, GYEONGGIDOLONGITUDE,
		// 	0.5, 0, 5);

		List<Restaurant> result = restaurantService.getAll();

		Map<String, Object> infos = new HashMap<>();
		infos.put("content", "점심시간 30분 전! 아래 식당을 추천드립니다!");
		// infos.put("username", "lunchBot123");
		// infos.put("avatar_url",
		// 	"https://www.urbanbrush.net/web/wp-content/uploads/edd/2018/04/web-20180414095809878760.png");

		List<Object> embeds = new ArrayList<>();

		Map<String, Object> embedsContent = new HashMap<>();
		embedsContent.put("color", 15258703);
		embedsContent.put("title", "오늘의 점메추!");
		// embedsContent.put("image", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUhXhN5wgRcVa3R6WBEoMrT9MfCsyOl4OVpXNO6t_2cw&s");
		List<Map<String, Object>> fields = new ArrayList<>();

		for (Restaurant restaurant : result) {
			Map<String, Object> field = new HashMap<>();
			field.put("name", restaurant.getBusinessPlaceName());
			field.put("value", restaurant.getSanitationBusinessName());
			fields.add(field);
		}

		embedsContent.put("fields", fields);
		embeds.add(embedsContent);
		// embeds 내용을 embeds라는 키로 전체 밖에 넣기
		infos.put("embeds", embeds);

		send(Ut.json.mapToJSONObject(infos));
	}

	private void send(JSONObject object) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> entity = new HttpEntity<>(object.toString(), headers);
		restTemplate.postForObject(url, entity, String.class);
	}
}