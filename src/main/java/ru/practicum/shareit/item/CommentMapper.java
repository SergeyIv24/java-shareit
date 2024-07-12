package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentsDto mapToCommentDto(Comments comments) {
        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setId(comments.getId());
        commentsDto.setText(comments.getText());
        commentsDto.setAuthorName(comments.getAuthor().getName());
        commentsDto.setCreated(comments.getCreated());
        return commentsDto;
    }

    public static Comments mapToComments(CommentsDto commentsDto, Item item, User user) {
        Comments comments = new Comments();
        comments.setText(commentsDto.getText());
        comments.setItem(item);
        comments.setAuthor(user);
        return comments;
    }
}
