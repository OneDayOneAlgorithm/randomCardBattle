package com.randomCardBattle.controller;


import com.randomCardBattle.model.Card;
import com.randomCardBattle.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin(origins = "http://localhost:3000")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    // Create a new card
    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody Card card) {
        Card newCard = cardService.saveCard(card);
        return ResponseEntity.ok(newCard);
    }

    // Get all cards
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    // Get a card by ID
    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        Optional<Card> card = cardService.getCardById(id);
        return card.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a card
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable Long id, @RequestBody Card updatedCard) {
        Optional<Card> card = cardService.updateCard(id, updatedCard);
        return card.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a card
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        if (cardService.deleteCard(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}