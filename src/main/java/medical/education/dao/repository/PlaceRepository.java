package medical.education.dao.repository;

import java.util.List;
import java.util.Optional;
import medical.education.dao.model.PlaceEntity;
import medical.education.dto.PlaceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import spring.backend.library.dao.repository.BaseRepository;

public interface PlaceRepository extends BaseRepository<PlaceEntity, PlaceDTO, Long> {

  @Override
  @Query("select e from PlaceEntity e"
      + " where (e.id = :#{#dto.id} or :#{#dto.id} is null )"
      + " and (lower(e.address) like :#{#dto.address} or :#{#dto.address} is null )")
  Page<PlaceEntity> search(PlaceDTO dto, Pageable pageable);

  boolean existsByAddress(String address);

  Optional<List<PlaceEntity>> findByHealthFacilityId(Long id);
}
