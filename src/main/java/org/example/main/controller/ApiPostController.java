package org.example.main.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsPostDto;
import org.example.main.service.post.ModeSorting;
import org.example.main.service.post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/post")
@RequiredArgsConstructor
public class ApiPostController {

  private final PostService postService;

  @GetMapping()
  public ResponseEntity<Map> getPosts(
      @RequestParam(name = "offset") Integer offset, @RequestParam(name = "limit") Integer limit,
      @RequestParam(name = "mode") String modeSorting) {
    List<RsPostDto> listPosts = postService.getPosts(ModeSorting.valueOf(modeSorting));
    return ResponseEntity.ok(Map.of("count", listPosts.size(), "posts", listPosts.stream().skip(offset).limit(limit)));
  }

}
