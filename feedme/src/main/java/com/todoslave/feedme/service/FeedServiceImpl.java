package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.FeedModifyRequest;
import com.todoslave.feedme.DTO.FeedRequestDTO;
import com.todoslave.feedme.DTO.FeedResponseDTO;
import com.todoslave.feedme.domain.entity.Feed.Feed;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{

    private final FeedRepository feedRepository;

    @Override
    public FeedResponseDTO insertFeed(FeedRequestDTO feedRequestDTO) {
        Feed feed = new Feed();
        feed.setContent(feedRequestDTO.getContent());
        feed.setMember(SecurityUtil.getCurrentMember());
        feed.setDiaryDay(feedRequestDTO.getDiaryDate());
        feed.setNickname(SecurityUtil.getCurrentMember().getNickname());

        feedRepository.save(feed);
        return convertToDTO(feed);
    }

    @Override
    public FeedResponseDTO modifyFeed(int feedId, FeedModifyRequest feedModifyRequest) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed == null) {return null;}

        feed.setContent(feedModifyRequest.getContent());
        feed.setUpdatedAt(LocalDateTime.now());
        feedRepository.save(feed);
        return convertToDTO(feed);
    }

    @Override
    public boolean delectFeed(int feedId) {
        Feed feed = feedRepository.findById(feedId).orElse(null);
        if (feed == null) {return false;}
        if(SecurityUtil.getCurrentMember().equals(feed.getMember())) {
            feedRepository.delete(feed);
            return true;
        }
        return false;
    }

    public FeedResponseDTO convertToDTO(Feed feed) {
        FeedResponseDTO dto = new FeedResponseDTO();
        dto.setCreatedAt(LocalDateTime.now());
        dto.setId(feed.getId());
        dto.setImg(diaryImgPath(feed)); // 이미지 URL이 엔티티에 없다면 필요에 따라 설정
        dto.setContent(feed.getContent());
        dto.setAuthor(feed.getNickname());
        dto.setLikeCnt(String.valueOf(feed.getLikeCount()));
        return dto;
    }

    private String diaryImgPath(Feed feed) {
        int id = feed.getMember().getId();
        String day = String.valueOf(feed.getDiaryDay());
        return "http://localhost:8080/image/pictureDiary/" + id + "_" + day;
    }
}


