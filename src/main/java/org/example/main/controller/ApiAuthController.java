package org.example.main.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RegisterDto;
import org.example.main.dto.response.RsErrorDto;
import org.example.main.service.auth.AuthService;
import org.example.main.service.auth.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

  private final CaptchaService captchaService;

  private final AuthService authService;

  @GetMapping("/check")
  public ResponseEntity<Map> authCheck() {
    //TODO: сделать после разработки авторизации
    return ResponseEntity.ok(Map.of("result", false));
  }

  @GetMapping("/captcha")
  public Map generateCaptcha() {
    return captchaService.generateCaptcha();
  }

  @PostMapping("/register")
  public ResponseEntity<Map> register(@RequestBody RegisterDto registerDto) {
    RsErrorDto errors = authService.registerUser(registerDto);
    if (errors == null) {
      return ResponseEntity.ok(Map.of("result", true));
    } else {
      return ResponseEntity.ok(Map.of("result", false, "errors", errors));
    }
  }

}
