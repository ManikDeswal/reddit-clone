package com.reddit.clone.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reddit.clone.model.Post;
import com.reddit.clone.model.Subreddit;
import com.reddit.clone.model.User;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

     List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
    
} 
