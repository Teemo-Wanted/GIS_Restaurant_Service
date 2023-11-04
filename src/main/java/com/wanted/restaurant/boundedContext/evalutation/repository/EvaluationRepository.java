package com.wanted.restaurant.boundedContext.evalutation.repository;

import com.wanted.restaurant.boundedContext.evalutation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
}
