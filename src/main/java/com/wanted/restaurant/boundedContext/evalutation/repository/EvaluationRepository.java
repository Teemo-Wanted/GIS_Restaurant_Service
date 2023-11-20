package com.wanted.restaurant.boundedContext.evalutation.repository;

import java.util.List;

import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import com.wanted.restaurant.boundedContext.restaurant.entity.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {

	List<Evaluation> findAllByRestaurant(Restaurant restaurant);
}
