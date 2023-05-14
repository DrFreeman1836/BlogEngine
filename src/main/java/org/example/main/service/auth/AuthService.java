package org.example.main.service.auth;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.RegisterDto;
import org.example.main.dto.response.RsErrorDto;
import org.example.main.model.CaptchaCodes;
import org.example.main.model.User;
import org.example.main.repository.CaptchaRepository;
import org.example.main.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final CaptchaRepository captchaRepository;

  private final UserRepository userRepository;

  public RsErrorDto registerUser(RegisterDto registerDto) {
    RsErrorDto errors = new RsErrorDto();
    boolean result = true;
    if (!checkEmail(registerDto.getEmail())) {
      errors.setEmail("Передан невалидный email или указанный email уже существует");
      result = false;
    }
    if (checkPassword(registerDto.getPassword())) {
      errors.setPassword("Парль короче 6-ти символов");
      result = false;
    }
    if (checkCaptcha(registerDto.getCaptcha(), registerDto.getCaptchaSecret())) {
      errors.setCaptcha("Код с картинки введён неверно");
      result = false;
    }
    if (result) {
      registerNewUser(registerDto);
      return null;
    }
    return errors;
  }

  private void registerNewUser(RegisterDto registerDto) {
    User newUser = new User();
    newUser.setEmail(registerDto.getEmail());
    newUser.setPassword(registerDto.getPassword());
    newUser.setName(registerDto.getName());
    newUser.setIsModerator(false);
    newUser.setRegTime(new Date());
    userRepository.save(newUser);
  }

  private Boolean checkPassword(String password) {
    return password.length() < 6;
  }

  private Boolean checkEmail(String email) {
    return userRepository.findByEmail(email).isEmpty();
  }

  private Boolean checkCaptcha(String captcha, String captchaSecret) {
    Optional<CaptchaCodes> captchaOptional = captchaRepository.findBySecretCode(captchaSecret);
    return !(captchaOptional.isPresent() && captchaOptional.get().getCode().equals(captcha));
  }

}
