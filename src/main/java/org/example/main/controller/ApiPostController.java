package org.example.main.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RqModerationStatus;
import org.example.main.dto.request.RqPostDto;
import org.example.main.dto.request.RqVotesDto;
import org.example.main.dto.response.RsErrors;
import org.example.main.dto.response.RsPostByIdDto;
import org.example.main.dto.response.RsPostDto;
import org.example.main.model.ModerationStatus;
import org.example.main.service.post.ModeSorting;
import org.example.main.service.post.ModerationService;
import org.example.main.service.post.PostService;
import org.example.main.service.post.PostVotesService;
import org.example.main.service.post.StatusMode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;

  private final PostVotesService votesService;

  private final ModerationService moderationService;

  @GetMapping()
  public ResponseEntity<Map> getPosts(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "mode") String modeSorting) {
    List<RsPostDto> listPosts = postService.getPosts(ModeSorting.valueOf(modeSorting));
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @GetMapping("/search")
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

  @GetMapping("/my")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> getMyPost(@RequestParam(name = "offset") Integer offset,
      @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "status") String status,
      Principal principal) {
    List<RsPostDto> listPosts = postService.getMyPost(StatusMode.valueOf(status), principal);
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

  @PostMapping()
  @PreAuthorize("hasAnyRole('USER')")
  public ResponseEntity<Map> addPost(@RequestBody RqPostDto postDto, Principal principal) {
    RsErrors errors = postService.addPost(postDto, principal);
    if (errors == null) {
      return ResponseEntity.ok(Map.of("result", true));
    } else {
      return ResponseEntity.ok(Map.of("result", false, "errors", errors));
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> updatePost(@PathVariable Integer id, @RequestBody RqPostDto postDto, Principal principal) {
    RsErrors errors = postService.updatePost(id, postDto, principal);
    if (errors == null) {
      return ResponseEntity.ok(Map.of("result", true));
    } else {
      return ResponseEntity.ok(Map.of("result", false, "errors", errors));
    }
  }

  @PostMapping("/like")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> addLike(@RequestBody RqVotesDto votesDto, Principal principal) {
    return votesService.addLike(votesDto.getPostId(), principal) ? ResponseEntity.ok(Map.of("result", true)) :
        ResponseEntity.ok(Map.of("result", false));
  }

  @PostMapping("/dislike")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> addDislike(@RequestBody RqVotesDto votesDto, Principal principal) {
    return votesService.addDislike(votesDto.getPostId(), principal) ? ResponseEntity.ok(Map.of("result", true)) :
        ResponseEntity.ok(Map.of("result", false));
  }

  @GetMapping("/moderation")
  @PreAuthorize("hasAnyRole('MODERATOR')")
  public ResponseEntity<Map> getPostsToModeration(@RequestParam(name = "offset") Integer offset,
      @RequestParam(name = "limit") Integer limit, @RequestParam(name = "status") String status) {
    List<RsPostDto> listPosts = moderationService.getPostsToModeration(ModerationStatus.valueOf(status.toUpperCase()));
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts));
  }

}
