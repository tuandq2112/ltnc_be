package medical.education.dao.repository;

import medical.education.dao.model.ResultEntity;
import medical.education.dto.ResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.backend.library.dao.repository.BaseRepository;

public interface ResultRepository extends BaseRepository<ResultEntity, ResultDTO, Long> {

    @Override
    @Query(" select e from ResultEntity e where "
            + " (e.registerId = :#{#dto.registerId} or :#{#dto.registerId} is null) "
            + " and (e.subjectId = :#{#dto.subjectId} or :#{#dto.subjectId} is null) "
            + " and (e.studentId = :#{#dto.studentId} or :#{#dto.studentId} is null)"
            + " and (e.courseId = :#{#dto.courseId} or :#{#dto.courseId} is null)")
    Page<ResultEntity> search(ResultDTO dto, Pageable pageable);

    @Query(" select case when count(e.id) > 0 then true else false end from ResultEntity e"
            + " where (1 = 1) "
            + " and (e.courseId = :courseId) "
            + " and (e.studentId = :studentId) "
            + " and (e.subjectId = :subjectId) ")
    public Boolean resultExistByCourseIdAndStudentIdAndSubjectId(@Param("courseId") Long courseId,
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId);
}
