package com.reddit.clone.Mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.reddit.clone.DTO.SubredditDto;
import com.reddit.clone.model.Post;
import com.reddit.clone.model.Subreddit;



@Mapper(componentModel = "Spring")
public interface SubredditMapper {

    @Mapping(target="numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")

    SubredditDto mapSubredditToDto (Subreddit subreddit);
    
    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target="posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto);
}
