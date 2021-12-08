package medical.education.dao.repository;

import java.util.List;
import medical.education.dao.model.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import spring.backend.library.dao.repository.BaseRepository;
import medical.education.dto.SubjectDTO;

public interface SubjectRepository extends BaseRepository<SubjectEntity, SubjectDTO, Long> {
  @Override
  @Query("select e from SubjectEntity e "
      + " where (lower(e.name) like :#{#dto.name} or :#{#dto.name} is null) "
      + " and (lower(e.code) like :#{#dto.code} or :#{#dto.code} is null) "
      + " and (e.type = :#{#dto.type} or :#{#dto.type} is null)"
      + " and (e.lesson = :#{#dto.lesson} or :#{#dto.lesson} is null)"
      )
  Page<SubjectEntity> search(SubjectDTO dto, Pageable pageable);

  @Query(" select count(e) from SubjectEntity e where e.deleted = 0 ")
  Integer countAll();

  boolean existsByNameAndTypeAndLesson(String name, String type, Integer lesson);

  @Query(" select distinct e.name from SubjectEntity e where e.deleted = 0")
  List<String> getDistinctSubject();
}
