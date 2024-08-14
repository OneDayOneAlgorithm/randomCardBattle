package com.randomCardBattle.repository;


import com.randomCardBattle.model.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
    // 덱 내에 있는 카드들만 가져오는 메서드
    List<GameDeck> findByInDeckTrue();
}