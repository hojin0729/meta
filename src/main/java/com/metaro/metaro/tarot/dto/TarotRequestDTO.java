package com.metaro.metaro.tarot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TarotRequestDTO {

    @JsonProperty("choice_master")
    private boolean choiceMaster;

    private String agenda;

    @JsonProperty("input_text")
    private String inputText;

    private List<Card> card;
    private List<String> history;

    @Data
    public static class Card {
        private int cardNumber;
        private boolean isReversed;

        @JsonCreator
        public Card(@JsonProperty("cardNumber") int cardNumber, @JsonProperty("isReversed") boolean isReversed) {
            this.cardNumber = cardNumber;
            this.isReversed = isReversed;
        }

        @JsonCreator
        public static Card fromArray(Object[] arr) {
            return new Card((int) arr[0], (boolean) arr[1]);
        }
    }
}