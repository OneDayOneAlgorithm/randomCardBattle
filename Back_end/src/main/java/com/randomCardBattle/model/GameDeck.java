package com.randomCardBattle.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GameDeck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameDeckID;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "cardID")
    private Card card;

    private boolean inDeck;
}