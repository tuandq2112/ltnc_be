package medical.education.dao.repository;


import medical.education.dao.model.ProgramEntity;
import medical.education.dto.ProgramDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import spring.backend.library.dao.repository.BaseRepository;

public interface ProgramRepository extends BaseRepository<ProgramEntity, ProgramDTO, Long> {

    @Override
    @Query(" select e from ProgramEntity e "
            + " where (e.id = :#{#dto.id} or :#{#dto.id} is null)"
            + " and (lower(e.name) like :#{#dto.name} or :#{#dto.name} is null)"
            + " and (lower(e.code) like :#{#dto.code} or :#{#dto.code} is null)"
            + " and (:#{#dto.numberTurn} = (select count(c) from CourseEntity c where (c.id = e.id))"
            + "   or :#{#dto.numberTurn} is null)")
    Page<ProgramEntity> search(ProgramDTO dto, Pageable pageable);

    @Query("select case when count (e) > 0 then true else false end from ProgramEntity e"
            + " where 1 = 1"
            + " and (e.code = :code)"
            + " and ( :id is null or e.id <> :id ) ")
    Boolean existsByCodeAndId(String code, Long id);

    @Query(value = "select count(*)"
            + " from register r"
            + "         left join course c on r.course_id = c.id"
            + "         left join program p on c.program_id = p.id"
            + " where p.id = ?1 and r.total >= 4", nativeQuery = true)
    Long getTongSoTotNghiep(Long id);

    @Query(value = "select count(*)"
            + " from register r"
            + "         left join course c on r.course_id = c.id"
            + "         left join program p on c.program_id = p.id"
            + " where p.id = ?1 and r.total < 4", nativeQuery = true)
    Long getTongSoTruot(Long id);

}
