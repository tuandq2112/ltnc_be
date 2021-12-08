package medical.education.dao.repository;

import medical.education.dao.model.HealthFacilityEntity;
import medical.education.dto.HealthFacilityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import spring.backend.library.dao.repository.BaseRepository;

public interface HealthFacilityRepository extends
    BaseRepository<HealthFacilityEntity, HealthFacilityDTO, Long> {

  @Override
  @Query("select e from HealthFacilityEntity e "
      + " where (lower(e.name) like :#{#dto.name} or :#{#dto.name} is null) "
      + " and (lower(e.address) like :#{#dto.address} or :#{#dto.address} is null) "
      + " and (lower(e.level) like :#{#dto.level} or :#{#dto.level} is null) "
      + " and (e.id = :#{#dto.id} or :#{#dto.id} is null) "
  )
  Page<HealthFacilityEntity> search(HealthFacilityDTO dto, Pageable pageable);

  boolean existsByName(String name);
}
