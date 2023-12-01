package com.reddit.clone.Service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reddit.Exceptions.PostNotFoundException;
import com.reddit.Exceptions.SpringRedditException;
import com.reddit.clone.DTO.VoteDto;
import com.reddit.clone.Repository.PostRepository;
import com.reddit.clone.Repository.VoteRepository;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.Vote;
import static com.reddit.clone.model.VoteType.UPVOTE;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void vote(VoteDto voteDto){
        Post post= postRepository.findById(voteDto.getPostId())
        .orElseThrow(()->new PostNotFoundException("Post not Found with ID- " + voteDto.getPostId()));
        Optional<Vote> voteByPostandUser=voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if(voteByPostandUser.isPresent() && voteByPostandUser.get().getVoteType().equals(voteDto.getVoteType())){
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");

        }
       
        if(UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount()+1);
        }
        else {
            post.setVoteCount(post.getVoteCount()-1);
        }
        
        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);

    }
    

     private Vote mapToVote(VoteDto voteDto,Post post){
            return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
        }
}
