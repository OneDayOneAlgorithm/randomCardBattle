package com.randomCardBattle.repository;

import com.randomCardBattle.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}