package medical.education.service;

import medical.education.dto.HealthFacilityDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import spring.backend.library.service.BaseService;

@PreAuthorize("hasAnyRole('ADMIN')")
public interface HealthFacilityService extends BaseService<HealthFacilityDTO> {

}
