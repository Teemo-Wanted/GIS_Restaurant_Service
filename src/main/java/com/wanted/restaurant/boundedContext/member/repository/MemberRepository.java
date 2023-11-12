package com.wanted.restaurant.boundedContext.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.restaurant.boundedContext.member.entity.AlarmType;
import com.wanted.restaurant.boundedContext.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByAccount(String account);

	List<Member> findAllByAlarmType(AlarmType alarmType);
}
