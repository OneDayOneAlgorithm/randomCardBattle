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

    private Long card1;
    private Long card2;
    private Long card3;
    private Long card4;
    private Long card5;
    private Long curseCard;
}