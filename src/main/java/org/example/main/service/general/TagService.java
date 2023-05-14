package org.example.main.service.general;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsTagDto;
import org.example.main.model.ModerationStatus;
import org.example.main.repository.PostRepository;
import org.example.main.repository.TagRepository;
import org.example.main.repository.TagRepository.TagGroupByDtoProjection;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  private final PostRepository postRepository;

  public List<RsTagDto> getTagsWeight(String query) {
    Integer countAllPosts = postRepository.countByIsActiveAndModerationStatusAndTimeBefore(true, ModerationStatus.ACCEPTED, new Date());
    Integer countPopularTag;
    List<TagGroupByDtoProjection> tagList;
    if (query == null) {
      tagList = tagRepository.getAllTagCountMap();
      countPopularTag = tagList.stream()
          .map(TagGroupByDtoProjection::getCount)
          .max(Comparator.comparingInt(Integer::intValue)).orElse(null);
    } else {
      tagList = tagRepository.getTagCountMap(query);
      countPopularTag = tagList.stream()
          .map(TagGroupByDtoProjection::getCount)
          .max(Comparator.comparingInt(Integer::intValue)).orElse(null);
    }
    List<RsTagDto> listWeight = new ArrayList<>();

    for (TagGroupByDtoProjection tag : tagList) {
      listWeight.add(RsTagDto.tagDtoBuilder(tag.getName(), calculateWeights(tag.getCount(), countAllPosts, countPopularTag)));
    }

    return listWeight;

  }

  private BigDecimal calculateWeights(Integer countTags, Integer countAllPosts, Integer countPopularTag) {
    BigDecimal dWight = BigDecimal.valueOf(countTags).divide(BigDecimal.valueOf(countAllPosts)).setScale(2);
    BigDecimal dWightMax = BigDecimal.valueOf(countPopularTag).divide(BigDecimal.valueOf(countAllPosts)).setScale(2);
    BigDecimal k = new BigDecimal("1.0").divide(dWightMax, 2, BigDecimal.ROUND_DOWN);
    return dWight.multiply(k).setScale(2, BigDecimal.ROUND_DOWN);
  }


}
