package com.randomCardBattle.dto;

import com.randomCardBattle.model.Card;
import com.randomCardBattle.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class GameResponseDTO {
    private Game game;
    private List<Card> player1Cards;
    private List<Card> player2Cards;
}