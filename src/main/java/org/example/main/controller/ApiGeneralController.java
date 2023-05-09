package org.example.main.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsGlobalSettingsDto;
import org.example.main.dto.response.RsInfoBlogDto;
import org.example.main.service.general.GlobalSettingService;
import org.example.main.service.general.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

}
