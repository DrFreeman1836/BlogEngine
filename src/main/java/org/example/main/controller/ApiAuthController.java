package org.example.main.controller;

import java.security.Principal;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.LoginRequest;
import org.example.main.dto.request.RegisterDto;
import org.example.main.dto.response.RsErrorDto;
import org.example.main.dto.response.RsLoginDto;
import org.example.main.service.auth.AuthService;
import org.example.main.service.auth.CaptchaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
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
  public ResponseEntity<Map> authCheck(Principal principal) {
    if (principal == null) {
      return ResponseEntity.ok(Map.of("result", false));
    } else {
      return ResponseEntity.ok(Map.of("result", true, "user", authService.checkLogin(principal)));
    }
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

  @PostMapping("/login")
  public ResponseEntity<Map> login(@RequestBody LoginRequest loginRequest) {
    RsLoginDto loginDto;
    try {
      loginDto = authService.loginUser(loginRequest);
    } catch (AuthenticationException e) {
      return ResponseEntity.ok(Map.of("result", false));
    }
    return ResponseEntity.ok(Map.of("result", true, "user", loginDto));
  }

  @GetMapping("/logout")
  @PreAuthorize("hasAnyRole('USER', 'MODERATOR')")
  public ResponseEntity<Map> logout(HttpServletRequest request) {
    try {
      request.logout();
    } catch (ServletException ignored) {}
    return ResponseEntity.ok(Map.of("result", true));
  }


}
