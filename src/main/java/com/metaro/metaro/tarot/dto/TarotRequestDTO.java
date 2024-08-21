package com.metaro.metaro.tarot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TarotRequestDTO {

    @JsonProperty("choice_master")
    private boolean choiceMaster;

    private String agenda;

    @JsonProperty("input_text")
    private String inputText;

    private List<Card> card;
    private List<String> history;

    @Getter
    @Setter
    public static class Card {
        private int cardNumber;

        @JsonProperty("isReversed")
        private boolean isReversed;

        // 기본 생성자
        public Card() {}

        public Card(int cardNumber, boolean isReversed) {
            this.cardNumber = cardNumber;
            this.isReversed = isReversed;
        }
    }
}