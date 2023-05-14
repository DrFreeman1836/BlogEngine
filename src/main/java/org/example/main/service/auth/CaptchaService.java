package org.example.main.service.auth;

import com.github.cage.Cage;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.example.main.model.CaptchaCodes;
import org.example.main.repository.CaptchaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaptchaService {

  private final CaptchaRepository captchaRepository;

  private static Integer width = 500;
  private static Integer height = 200;

  public Map generateCaptcha() {
    Cage captchaGenerator = new Cage();
    String captchaCode = captchaGenerator.getTokenGenerator().next();

    BufferedImage captchaImage = captchaGenerator.drawImage(captchaCode);
    BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = scaledImage.createGraphics();
    g2d.drawImage(captchaImage, 0, 0, width, height, null);
    g2d.dispose();
    String captchaImageBase64 = convertImageToBase64(captchaImage);

    String secret = UUID.randomUUID().toString();

    CaptchaCodes codes = new CaptchaCodes();
    codes.setTime(new Date());
    codes.setSecretCode(secret);
    codes.setCode(captchaCode);
    captchaRepository.save(codes);

    Map<String, String> response = new HashMap<>();
    response.put("secret", secret);
    response.put("image", captchaImageBase64);
    return response;
  }

  private String convertImageToBase64(BufferedImage image) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(image, "png", baos);
    } catch (IOException e) {
      e.printStackTrace();
    }
    byte[] imageBytes = baos.toByteArray();
    return "data:image/png;base64, " + Base64.getEncoder().encodeToString(imageBytes);
  }

  @Scheduled(fixedRate = 3_600_000)
  public void removeExpiredCaptchaCodes() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.HOUR, -1);
    captchaRepository.findAll(Sort.by(Sort.Direction.ASC, "time")).forEach(c -> {
      if (calendar.getTime().compareTo(c.getTime()) >= 0) {
        captchaRepository.deleteById(c.getId());
      }
    });
  }

}
