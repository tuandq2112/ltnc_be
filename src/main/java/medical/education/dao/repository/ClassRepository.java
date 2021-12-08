package medical.education.dao.repository;

import java.util.List;
import medical.education.dao.model.ClassEntity;
import medical.education.dto.ClassDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends CrudRepository<ClassEntity, Long> {

    @Query(value = "select u.id              as id,"
            + "             u.full_name      as name,"
            + "             rl.mid_point     as mid_point,"
            + "             rl.end_point     as end_point,"
            + "             rl.attendance    as muster,"
            + "             rl.total         as total,"
            + "             rl.subject_id    as subject_id,"
            + "             sj.lesson        as lesson"
            + "      from users u"
            + "           full join results rl on u.id = rl.student_id"
            + "           full join course cr on cr.id = u.current_course_id"
            + "           full join subject sj on sj.id = rl.subject_id"
            + " where (1 = 1)"
            + " and (cr.id = :#{#dto.courseId} and :#{#dto.courseId} <> -1)"
            + " and (rl.subject_id = :#{#dto.subjectId} and :#{#dto.subjectId} <> -1)"
            , nativeQuery = true)
    Page<ClassEntity> search(ClassDTO dto, Pageable pageable);

}
