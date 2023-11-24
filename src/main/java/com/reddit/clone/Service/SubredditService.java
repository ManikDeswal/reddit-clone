package com.reddit.clone.Service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reddit.Exceptions.SpringRedditException;
import com.reddit.clone.DTO.SubredditDto;
import com.reddit.clone.Mapper.SubredditMapper;
import com.reddit.clone.Repository.SubredditRepository;
import com.reddit.clone.model.Subreddit;
import static java.util.stream.Collectors.toList;
import lombok.AllArgsConstructor;
import java.util.List;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
   
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit save=subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }
    
    @Transactional(readOnly = true)
    public List<SubredditDto> getAll(){
        return subredditRepository.findAll()
                           .stream()
                           .map(subredditMapper::mapSubredditToDto)
                           .collect(toList());
    }

    
    public SubredditDto getSubreddit(Long id){
        Subreddit subreddit=subredditRepository.findById(id)
        .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
