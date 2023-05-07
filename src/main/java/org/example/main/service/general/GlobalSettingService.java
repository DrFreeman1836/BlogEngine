package org.example.main.service.general;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ListIterator;
import lombok.RequiredArgsConstructor;
import org.example.main.dto.response.RsGlobalSettingsDto;
import org.example.main.model.GlobalSettings;
import org.example.main.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GlobalSettingService {

  private final GlobalSettingsRepository settingRepo;

  private final ObjectMapper mapper = new ObjectMapper();

  public RsGlobalSettingsDto getSettings() throws JsonProcessingException {
    List<GlobalSettings> listSettings =  settingRepo.findAll();
    String dtoString = dtoBuilder(listSettings);
    return mapper.readValue(dtoString, RsGlobalSettingsDto.class);
  }

  private String dtoBuilder(List<GlobalSettings> settings) {
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    ListIterator<GlobalSettings> it = settings.listIterator();
    while (it.hasNext()) {
      GlobalSettings gs = it.next();
      sb.append("\"").append(gs.getCode()).append("\"");
      sb.append(": ");
      sb.append(gs.getValue().equalsIgnoreCase("no") ? "false" : "true");
      if (it.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("}");
    return sb.toString();
  }

}
