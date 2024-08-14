package com.randomCardBattle.repository;


import com.randomCardBattle.model.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
}