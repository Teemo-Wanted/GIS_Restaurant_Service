package com.wanted.restaurant.boundedContext.sigungu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.restaurant.boundedContext.sigungu.entity.Sigungu;

public interface SigunguRepository extends JpaRepository<Sigungu, Long> {
	Sigungu findByDosiContainingAndSigunguContaining(String doSi, String sgg);

	Sigungu findByLongitudeAndLatitude(Double lng, Double lat);
}
