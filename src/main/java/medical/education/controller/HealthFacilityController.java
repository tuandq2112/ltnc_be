package medical.education.controller;

import medical.education.dto.HealthFacilityDTO;
import medical.education.service.HealthFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;

@CrossOrigin
@RestController
@RequestMapping("/health-facility")
public class HealthFacilityController extends BaseController<HealthFacilityDTO, HealthFacilityService> {
  @Autowired
  private HealthFacilityService healthFacilityService;

  @Override
  public HealthFacilityService getService() {
    return healthFacilityService;
  }
}
