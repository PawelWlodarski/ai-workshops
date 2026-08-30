package com.wlodar.jug.ai.examples.boardgame;

import jakarta.validation.constraints.NotBlank;


public record Question(
       @NotBlank(message = "Game title cannot be blank")   String gameTitle,
       @NotBlank(message = "Question cannot be blank") String question
       ) {

}
