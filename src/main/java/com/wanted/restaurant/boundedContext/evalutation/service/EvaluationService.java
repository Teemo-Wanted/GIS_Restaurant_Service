package com.wanted.restaurant.boundedContext.evalutation.service;

import com.wanted.restaurant.base.rsData.RsData;
import com.wanted.restaurant.boundedContext.evalutation.dto.EvaluateRequestDto;
import com.wanted.restaurant.boundedContext.evalutation.dto.EvaluateResponseDto;
import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import com.wanted.restaurant.boundedContext.evalutation.repository.EvaluationRepository;
import com.wanted.restaurant.boundedContext.member.entity.Member;
import com.wanted.restaurant.boundedContext.member.repository.MemberRepository;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;
import com.wanted.restaurant.boundedContext.restaurant.repository.RestaurantRepository;
import com.wanted.restaurant.boundedContext.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    /**
     * 평가 생성
     */
    @Transactional
    public RsData evaluate(EvaluateRequestDto requestDto) {
        Optional<Member> memberOptional = memberRepository.findById(requestDto.getMemberId());
        if (memberOptional.isEmpty())
            return RsData.of("EVALUATE_ERROR", "유효한 memberId가 아닙니다.");
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(requestDto.getRestaurantId());
        if (restaurantOptional.isEmpty())
            return RsData.of("EVALUATE_ERROR", "유효한 restaurantId가 아닙니다.");
        Evaluation evaluation = Evaluation.builder()
                .member(memberOptional.get())
                .restaurant(restaurantOptional.get())
                .score(requestDto.getScore())
                .content(requestDto.getContent())
                .build();
        evaluationRepository.save(evaluation);

        //맛집에 대해 평접 업데이트(맛집 모든 평가 기록 조회 및 평균 계산)
        restaurantService.updateGrade(restaurantOptional.get());

        return new RsData("EVALUATION_CREATED", "평가 생성 완료", new EvaluateResponseDto(evaluation));
    }
}
