package org.example.main.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsPostByIdDto;
import org.example.main.dto.response.RsPostDto;
import org.example.main.service.post.ModeSorting;
import org.example.main.service.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;

  @GetMapping()
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> getPosts(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "mode") String modeSorting) {
    List<RsPostDto> listPosts = postService.getPosts(ModeSorting.valueOf(modeSorting));
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @GetMapping("/search")
  @PreAuthorize("hasAnyRole('MODERATOR')")
  public ResponseEntity<Map> searchPosts(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "query") String query
  ) {
    List<RsPostDto> listPosts = postService.searchPosts(query);
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @GetMapping("/byDate")
  public ResponseEntity<Map> getPostsByDate(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "date") String dateString
  ) {
    List<RsPostDto> listPosts = postService.getPostsByDate(dateString);
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @GetMapping("/byTag")
  public ResponseEntity<Map> getPostsByTag(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "tag") String tag
  ) {
    List<RsPostDto> listPosts = postService.getPostsByTag(tag);
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<RsPostByIdDto> getPostById(@PathVariable Integer id) {
    RsPostByIdDto post = postService.getPostById(id);
    if (post != null) {
      return ResponseEntity.ok(post);
    } else {
      return ResponseEntity.status(404).build();
    }
  }

}
