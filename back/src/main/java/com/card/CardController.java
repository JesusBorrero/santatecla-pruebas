package com.card;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api
public interface CardController {
    @ApiOperation(value = "Return all the cards of the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cards"),
            @ApiResponse(code = 404, message = "Unit Not Found")
    })
    ResponseEntity<Iterable<Card>> getCards(int unitId);

    @ApiOperation(value = "Return the card in the unit with the requested unitId and with the required cardId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Required card"),
            @ApiResponse(code = 404, message = "Card Not Found")
    })
    ResponseEntity<Card> getCard(long unitId, long cardId);


    @ApiOperation(value = "Return a group of cards containing the required name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Required cards"),
            @ApiResponse(code = 200, message = "Cards Not Found")
    })
    ResponseEntity<List<Card>> getCardByName(String unitName, String cardName);

    @ApiOperation(value = "Create a new Card in the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Card has been successfully created"),
            @ApiResponse(code = 404, message = "Unit Not Found")
    })
    ResponseEntity<Card> createCard(long unitId, CardDto cardDto);

    @ApiOperation(value = "Upload the card with the requested cardId and in the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Card has been successfully updated"),
            @ApiResponse(code = 404, message = "Unit or Card Not Found")
    })
    ResponseEntity<Card> uploadCard(long unitId, long cardId, CardDto cardDto);

    @ApiOperation(value = "Delete the card with the requested cardId and in the unit with the requested unitId.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The Card has been successfully deleted"),
            @ApiResponse(code = 404, message = "Unit or Card Not Found")
    })
    ResponseEntity<Card> deleteCard(long unitId, long cardId);


}
