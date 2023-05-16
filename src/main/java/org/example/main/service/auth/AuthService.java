package org.example.main.service.auth;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.request.LoginRequest;
import org.example.main.dto.request.RegisterDto;
import org.example.main.dto.response.RsErrorDto;
import org.example.main.dto.response.RsLoginDto;
import org.example.main.model.CaptchaCodes;
import org.example.main.model.ModerationStatus;
import org.example.main.model.User;
import org.example.main.repository.CaptchaRepository;
import org.example.main.repository.PostRepository;
import org.example.main.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final CaptchaRepository captchaRepository;

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final PostRepository postRepository;

  private final PasswordEncoder passwordEncoder;

  public RsLoginDto checkLogin(Principal principal) {
    return getLoginUser(principal.getName());
  }

  public User getCurrentUser(Principal principal) {
    return userRepository.findByEmail(principal.getName()).get();
  }

  public RsLoginDto loginUser(LoginRequest loginRequest) throws AuthenticationException {
    Authentication auth =
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
            (loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(auth);
    org.springframework.security.core.userdetails.User user =
        (org.springframework.security.core.userdetails.User) auth.getPrincipal();

    return getLoginUser(user.getUsername());
  }

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

  private RsLoginDto getLoginUser(String email) {
    User userCurrent = userRepository.findByEmail(email).get();
    RsLoginDto rsLoginDto = new RsLoginDto();
    rsLoginDto.fillFields(userCurrent, postRepository.countByModerationStatus(ModerationStatus.NEW));
    return rsLoginDto;
  }

  private void registerNewUser(RegisterDto registerDto) {
    User newUser = new User();
    newUser.setEmail(registerDto.getEmail());
    newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
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
