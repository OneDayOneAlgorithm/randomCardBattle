package com.randomCardBattle.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GameAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionID;

    @ManyToOne
    @JoinColumn(name = "memberID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;

    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;
    private String curseCard;
}