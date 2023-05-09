package org.example.main.repository;

import java.util.Date;
import java.util.List;
import org.example.main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

  @Query("select distinct t "
      + "from Tag t "
      + "join TagToPost tp on t.id = tp.tag.id "
      + "join Post p on p.id = tp.post.id "
      + "where p.isActive = true and "
      + "p.moderationStatus = 'ACCEPTED' and "
      + "p.time <= :time")
  List<Tag> findTagsByActualPost(@Param("time") Date time);

  @Query("select distinct t "
      + "from Tag t "
      + "join TagToPost tp on t.id = tp.tag.id "
      + "join Post p on p.id = tp.post.id "
      + "where p.isActive = true and "
      + "p.moderationStatus = 'ACCEPTED' and "
      + "p.time <= current_date()")
  List<Tag> findTagsByActualPost();

  @Query(value = "select tags.name, count(*) as count "
      + "from tags "
      + "join tag_to_post on tags.id = tag_to_post.tag_id "
      + "join posts on posts.id = tag_to_post.post_id "
      + "where posts.is_active = true and "
      + "posts.moderation_status = 'ACCEPTED' and "
      + "posts.time <= current_date() "
      + "group by tags.name", nativeQuery = true)
  List<TagGroupByDtoProjection> getAllTagCountMap();

  @Query(value = "select tags.name, count(*) as count "
      + "from tags "
      + "join tag_to_post on tags.id = tag_to_post.tag_id "
      + "join posts on posts.id = tag_to_post.post_id "
      + "where posts.is_active = true and "
      + "posts.moderation_status = 'ACCEPTED' and "
      + "posts.time <= current_date() and "
      + "tags.name like concat(:tag_name, '%') "
      + "group by tags.name", nativeQuery = true)
  List<TagGroupByDtoProjection> getTagCountMap(@Param("tag_name") String tagName);

  interface TagGroupByDtoProjection {
    String getName();
    Integer getCount();
  }

}
