package com.reddit.clone.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.reddit.Exceptions.PostNotFoundException;
import com.reddit.clone.DTO.CommentsDto;
import com.reddit.clone.Mapper.CommentMapper;
import com.reddit.clone.Repository.CommentRepository;
import com.reddit.clone.Repository.PostRepository;
import com.reddit.clone.Repository.UserRepository;
import com.reddit.clone.model.Comment;
import com.reddit.clone.model.NotificationEmail;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private static final String POST_URL = "";

    public void save(CommentsDto commentsDto){
        Post post=postRepository.findById(commentsDto.getPostId())
        .orElseThrow(()->new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment=commentMapper.map(commentsDto, authService.getCurrentUser(), post);
        commentRepository.save(comment);

        String message= mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post " + POST_URL);
        sendCommentNotification(message,post.getUser());
    }
    private void sendCommentNotification(String message, User user){
        mailService.sendMail(new NotificationEmail(user.getUsername()+" Commented on your post ",user.getEmail(),message));
    }

    public List<CommentsDto> getAllCommentsforPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());

    }
    public List<CommentsDto> getAllCommentsforUser(Long userName){
        User user= userRepository.findById(userName).orElseThrow(()->new UsernameNotFoundException(userName.toString()));
        return commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(Collectors.toList());

    }
}
