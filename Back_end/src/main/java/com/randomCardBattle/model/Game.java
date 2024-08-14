package com.randomCardBattle.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameID;

    @ManyToOne
    @JoinColumn(name = "player1ID")
    private Member player1;

    @ManyToOne
    @JoinColumn(name = "player2ID")
    private Member player2;
    private String startTime;
    private String endTime;
    private Long player1Health;
    private Long player2Health;
    private Long player1CurseCardId;
    private Long player2CurseCardId;
    private Long currentDamage;
    private Long totalDamage;
    private Long currentTurnPlayerID;
    private Long winnerID;
}