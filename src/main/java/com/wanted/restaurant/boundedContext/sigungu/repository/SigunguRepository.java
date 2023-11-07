package com.wanted.restaurant.boundedContext.sigungu.repository;

import com.wanted.restaurant.boundedContext.sigungu.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
	Sigungu findByDosiContainingAndSigunguContaining(String doSi, String sgg);
}
