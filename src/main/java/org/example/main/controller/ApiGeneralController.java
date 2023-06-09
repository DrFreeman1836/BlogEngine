package org.example.main.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RqCommentDto;
import org.example.main.dto.request.RqModerationStatus;
import org.example.main.dto.response.RsErrors;
import org.example.main.dto.response.RsGlobalSettingsDto;
import org.example.main.dto.response.RsInfoBlogDto;
import org.example.main.service.general.CalendarService;
import org.example.main.service.general.CommentService;
import org.example.main.service.general.GlobalSettingService;
import org.example.main.service.general.TagService;
import org.example.main.service.post.ModerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class ApiGeneralController {

  private final GlobalSettingService settingsService;

  private final RsInfoBlogDto infoBlog;

  private final TagService tagService;

  private final CalendarService calendarService;

  private final CommentService commentService;

  private final ModerationService moderationService;

  @GetMapping("/init")
  public ResponseEntity<RsInfoBlogDto> getInfoOfBlog() {
    return ResponseEntity.ok(infoBlog);
  }

  @GetMapping("/settings")
  public ResponseEntity<RsGlobalSettingsDto> getGlobalSettings() {
    try {
      RsGlobalSettingsDto response = settingsService.getSettings();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(400).body(null);
    }
  }

  @GetMapping("/tag")
  public ResponseEntity<Map> getTags(@RequestParam(name = "query", required = false) String query) {
    return ResponseEntity.ok(Map.of("tags", tagService.getTagsWeight(query)));
  }

  @GetMapping("/calendar")
  public ResponseEntity<Map> countPostByDate(@RequestParam(name = "year", required = false) Integer year) {
    HashMap<String, List> response = new HashMap<>();
    response.put("years", calendarService.getAllYearPublish());
    response.put("posts", calendarService.getGroupPostsByDate(year));
    return ResponseEntity.ok(response);
  }

  @PostMapping("/comment")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> addComment(@RequestBody RqCommentDto commentDto, Principal principal) {
    RsErrors errors = commentService.addCommentToPost(commentDto, principal);
    if (errors == null) {
      return ResponseEntity.ok(Map.of("result", true));
    } else {
      return ResponseEntity.status(400).body(Map.of("result", false, "errors", errors));
    }
  }

  @PostMapping("/moderation")
  @PreAuthorize("hasAnyRole('MODERATOR')")
  public ResponseEntity<Map> setModerationStatus(@RequestBody RqModerationStatus moderationStatus, Principal principal) {
    Boolean result = moderationService.setModerationStatus(moderationStatus, principal);
    return ResponseEntity.ok(Map.of("result", result));
  }

}
