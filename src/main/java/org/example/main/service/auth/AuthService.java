package org.example.main.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.main.repository.CaptchaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final CaptchaRepository captchaRepository;

  public void generateCaptcha() {

  }

}
