package com.example.demo.Controller;

import com.example.demo.Model.Cards;
import com.example.demo.Repository.CardRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardRepository cardRepository;

    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/{cardId}")
    public Cards getCard(@PathVariable long cardId) throws SQLException {
        return cardRepository.findById(cardId);
    }

    @GetMapping("/account/{accountId}")
    public List<Cards> getCardsForAccount(@PathVariable long accountId) throws SQLException {
        return cardRepository.findByAccountId(accountId);
    }

    @PostMapping
    public long issueCard(@RequestBody Cards card) throws SQLException {
        return cardRepository.save(card);
    }

    @PatchMapping("/{cardId}/status")
    public void updateCardStatus(@PathVariable long cardId, @RequestParam String status) throws SQLException {
        cardRepository.updateStatus(cardId, status);
    }
}