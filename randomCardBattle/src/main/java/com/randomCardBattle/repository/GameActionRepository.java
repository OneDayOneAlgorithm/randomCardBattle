package com.randomCardBattle.repository;

import com.randomCardBattle.model.GameAction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameActionRepository extends JpaRepository<GameAction, Long> {
}