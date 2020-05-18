package com.card;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
    
    public List<Card> findAll();

    @Query(value = "select card.id, card.name, card.content from card right join unit_cards on card.id = unit_cards.cards_id left join unit on unit.id = unit_cards.unit_id where card.name = ?2 and unit.name = ?1", nativeQuery = true)
    public List<Card> findByName(String unitName, String cardName);
    
}