package org.example.main.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

  private final AuthService authService;

  @GetMapping("/check")
  public ResponseEntity<Map> authCheck() {
    //TODO: сделать после разработки авторизации
    return ResponseEntity.ok(Map.of("result", false));
  }

  @GetMapping("/captcha")
  public String generateCaptcha() {
    authService.generateCaptcha();
    return null;
  }

}
