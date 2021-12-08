package medical.education.dao.repository;

import java.util.Optional;
import medical.education.dao.model.UserEntity;
import medical.education.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.backend.library.dao.repository.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, UserDTO, Long> {

  @Query("select case when count(e) > 0 then true else false end from UserEntity e"
      + " where e.username = :#{#username}"
      + " and (e.id <> :#{#id} or :#{#id} is null)")
  boolean existsByUsername(String username,Long id);

  Optional<UserEntity> findByUsername(String username);

  @Override
  @Query("select e from UserEntity e"
      + " where (e.id = :#{#dto.id} or :#{#dto.id} is null)"
      + " and (e.role = :#{#dto.role} or :#{#dto.role} is null)"
      + " and (e.status = :#{#dto.status} or :#{#dto.status} is null)"
      + " and (e.idChange <> -1 or e.idChange is null)"
      + " and (e.age = :#{#dto.age} or :#{#dto.age} is null)"
      + " and (e.currentCourseId = :#{#dto.currentCourseId} or :#{#dto.currentCourseId} is null)"
      + " and (e.gender = :#{#dto.gender} or :#{#dto.gender} is null)"
      + " and (lower(e.username) like :#{#dto.username} or :#{#dto.username} is null)"
      + " and (lower(e.address) like :#{#dto.address} or :#{#dto.address} is null)"
      + " and (lower(e.fullName) like :#{#dto.fullName} or :#{#dto.fullName} is null)"
      + " and (e.phoneNumber like :#{#dto.phoneNumber} or :#{#dto.phoneNumber} is null)"
      + " and (lower(e.email) like :#{#dto.email} or :#{#dto.email} is null)"
      + " and (exists (select 1 from e.subjects subject where :#{#dto.subjectId} = subject.id) or :#{#dto.subjectId} is null)")
  Page<UserEntity> search(UserDTO dto, Pageable pageable);
}
