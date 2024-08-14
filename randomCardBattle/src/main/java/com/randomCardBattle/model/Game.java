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
    private int player1Health;
    private int player2Health;
    private int currentDamage;
    private int currentTurnPlayerID;
    private Long winnerID;
}