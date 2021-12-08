package medical.education.service;

import com.google.common.base.Strings;
import medical.education.dao.model.PlaceEntity;
import medical.education.dao.repository.HealthFacilityRepository;
import medical.education.dao.repository.PlaceRepository;
import medical.education.dto.PlaceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.service.AbstractBaseService;

@Service
//@PreAuthorize("hasAnyRole('ADMIN')")
public class PlaceServiceImpl extends AbstractBaseService<PlaceEntity, PlaceDTO, PlaceRepository>
    implements PlaceService {

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private HealthFacilityRepository healthFacilityRepository;

  @Override
  protected PlaceRepository getRepository() {
    return placeRepository;
  }

  @Override
  protected void beforeSave(PlaceEntity entity, PlaceDTO dto) {
    super.beforeSave(entity, dto);
    if (Strings.isNullOrEmpty(dto.getAddress())) {
      throw new BaseException(400, "address is null or empty");
    }
    if (dto.getHealthFacilityId() == null || !healthFacilityRepository
        .existsById(dto.getHealthFacilityId())) {
      throw new BaseException(400, "healthFacilityId is null or empty");
    }
    if (getRepository().existsByAddress(dto.getAddress()) && dto.getId() == null) {
      throw new BaseException(400, "địa điểm đã tồn tại");
    }
  }

  @Override
  public Page<PlaceDTO> search(PlaceDTO dto, Pageable pageable) {
    if (dto.getAddress() != null) {
      dto.setAddress('%' + dto.getAddress().trim().toLowerCase().replace(' ', '%') + '%');
    }
    return super.search(dto, pageable);
  }
}
