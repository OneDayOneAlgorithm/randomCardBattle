package com.randomCardBattle.repository;


import com.randomCardBattle.model.Game;
import com.randomCardBattle.model.GameDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameDeckRepository extends JpaRepository<GameDeck, Long> {
    // 덱 내에 있는 카드들만 가져오는 메서드
    @Query("SELECT gd FROM GameDeck gd WHERE gd.game = :game AND gd.inDeck = true ORDER BY FUNCTION('RANDOM')")
    List<GameDeck> findByGameAndInDeckTrue(@Param("game") Game game);

}