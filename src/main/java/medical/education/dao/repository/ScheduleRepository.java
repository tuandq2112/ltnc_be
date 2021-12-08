package medical.education.dao.repository;

import java.util.List;
import java.util.Optional;
import medical.education.dao.model.ScheduleEntity;
import medical.education.dto.ScheduleDTO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import spring.backend.library.dao.repository.BaseRepository;

public interface ScheduleRepository extends BaseRepository<ScheduleEntity, ScheduleDTO, Long> {

  @Override
  @Query("select e from ScheduleEntity e "
      + " where (e.id = :#{#dto.id} or :#{#dto.id} is null) "
      + " and (e.courseId = :#{#dto.courseId} or :#{#dto.courseId} is null) "
      + " and (e.status = 0) "
      + " and (e.teacherId = :#{#dto.teacherId} or :#{#dto.teacherId} is null) ")
  Page<ScheduleEntity> search(ScheduleDTO dto, Pageable pageable);

  @Query("select e from ScheduleEntity e"
      + " where 1 = 1"
      + " and (e.changeScheduleId is not null or e.reason is not null)")
  Page<ScheduleEntity> findAllChange(Pageable page);

  @Query("select count(e) from ScheduleEntity e"
      + " where 1 = 1"
      + " and (e.changeScheduleId is not null or e.reason is not null)")
  Integer countChange();


  @Query("select case when count(e) > 0 then true else false end from ScheduleEntity e "
      + "where 1=1 "
      + "and (e.kipHoc = :kipHoc)"
      + "and (e.id <> :id or :id is null) "
      + "and (e.day = :day)")
  Boolean checkExistByDayAndKipHoc(Short kipHoc, Short day, Long id);

  //  @Query("select case when count(e) > 0 then true else false end from ScheduleEntity e "
//      + "where 1=1 "
//      + " and (e.courseId = :#{#dto.courseId} or :#{#dto.courseId} is null) ")
  Boolean existsByCourseIdAndSubjectId(Long courseId, Long subjectId);

  Optional<List<ScheduleEntity>> findByTeacherId(Long id);
}
