package ru.practicum.shareit.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * User`s review after booking, this model is mentioned in technical task
 */

@Data
public class Review {
    Long reviewId;
    @NotBlank(message = "Empty content")
    @Size(min = 2, max = 300, message = "Bad content length")
    String content;
    Boolean isTaskDone; //Confirmation that item done its function
}
