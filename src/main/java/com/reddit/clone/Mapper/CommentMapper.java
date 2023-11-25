package com.reddit.clone.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.clone.DTO.CommentsDto;
import com.reddit.clone.model.Comment;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text",source = "commentsDto.text")
    @Mapping(target = "createDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDto commentsDto,User user,Post post);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
    
} 
