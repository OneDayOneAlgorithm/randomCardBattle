package com.randomCardBattle.service;

import com.randomCardBattle.model.Card;
import com.randomCardBattle.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    // Save a new card
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    // Get all cards
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Get a card by ID
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    // Update a card
    public Optional<Card> updateCard(Long id, Card updatedCard) {
        return cardRepository.findById(id).map(existingCard -> {
            existingCard.setCardType(updatedCard.getCardType());
            existingCard.setDescription(updatedCard.getDescription());
            return cardRepository.save(existingCard);
        });
    }

    // Delete a card
    public boolean deleteCard(Long id) {
        return cardRepository.findById(id).map(card -> {
            cardRepository.delete(card);
            return true;
        }).orElse(false);
    }
}
