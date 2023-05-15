package org.example.main.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.example.main.model.ModerationStatus;
import org.example.main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  List<Post> findByIsActiveAndModerationStatusAndTimeBefore(Boolean isActive, ModerationStatus moderationStatus, Date time);
  Integer countByIsActiveAndModerationStatusAndTimeBefore(Boolean isActive, ModerationStatus moderationStatus, Date time);
  List<Post> findByIsActiveAndModerationStatusAndTimeBeforeAndTextContaining(Boolean isActive, ModerationStatus moderationStatus, Date time, String text);

  @Query(value = "select p "
      + "from Post p "
      + "where p.isActive = true "
      + "and p.moderationStatus = 'ACCEPTED' "
      + "and p.time <= current_timestamp() "
      + "and p.id = :id")
  Optional<Post> findActivePostById(@Param("id") Integer id);

  @Query(value = "select * "
      + "from posts "
      + "where is_active = 1 and "
      + "moderation_status = 'ACCEPTED' and "
      + "time <= current_timestamp() and "
      + "date(time) = :datePost", nativeQuery = true)
  List<Post> getPostsByDate(@Param("datePost") String datePost);

  @Query(value = "select distinct year(time) from posts where time <= current_timestamp()", nativeQuery = true)
  List<Integer> getAllYearPublish();

  @Query(value = "select date(time) as date, count(*) as count "
      + "from posts "
      + "where is_active = 1 and "
      + "moderation_status = 'ACCEPTED' and "
      + "time <= current_timestamp() and "
      + "year(time) = :year "
      + "group by date(time)", nativeQuery = true)
  List<PostGroupByDtoProjection> getGroupPostsByDate(@Param("year") Integer year);

  Integer countByModerationStatus(ModerationStatus moderationStatus);

  interface PostGroupByDtoProjection {

    Date getDate();

    Integer getCount();

  }

}
