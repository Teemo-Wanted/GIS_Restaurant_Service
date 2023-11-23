package com.wanted.restaurant.boundedContext.evalutation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

	List<Evaluation> findAllByRestaurant(Restaurant restaurant);
}
