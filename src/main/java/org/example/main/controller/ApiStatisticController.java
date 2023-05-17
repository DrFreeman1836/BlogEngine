package org.example.main.controller;

import java.security.Principal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.service.statistic.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/statistics")
@RequiredArgsConstructor
public class ApiStatisticController {

  private final StatisticService statisticService;

  @GetMapping("/my")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<?> getMyStatistic(Principal principal) {
    return ResponseEntity.ok(statisticService.getMyStatistic(principal));
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllStatistic() {
    return ResponseEntity.ok(statisticService.getAllStatistic());
  }

}
