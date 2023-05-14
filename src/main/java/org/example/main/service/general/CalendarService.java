package org.example.main.service.general;

import java.time.Year;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.main.repository.PostRepository;
import org.example.main.repository.PostRepository.PostGroupByDtoProjection;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarService {

  private final PostRepository postRepository;

  public List<Integer> getAllYearPublish() {
    return postRepository.getAllYearPublish();
  }

  public List<PostGroupByDtoProjection> getGroupPostsByDate(Integer year) {
    if (year == null) {
      return postRepository.getGroupPostsByDate(Year.now().getValue());
    } else {
      return postRepository.getGroupPostsByDate(year);
    }
  }

}
