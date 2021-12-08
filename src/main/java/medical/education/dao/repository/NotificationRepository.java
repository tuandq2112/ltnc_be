package medical.education.dao.repository;

import medical.education.dao.model.NotificationEntity;
import medical.education.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import spring.backend.library.dao.repository.BaseRepository;

public interface NotificationRepository extends
    BaseRepository<NotificationEntity, NotificationDTO, Long> {

  @Query(value = "select e from NotificationEntity e"
      + " where (e.deleted = 0 )"
      + " and (e.id = :#{#dto.id} or :#{#dto.id} is null) "
      + " and (e.content like :#{#dto.content} or :#{#dto.content} is null)"
      + " and (e.isRead = :#{#dto.isRead} or :#{#dto.isRead} is null)"
      + " and (e.ownerId = :#{#dto.ownerId} or :#{#dto.ownerId} is null)"
      + " and (e.role = :#{#dto.role} or :#{#dto.role} is null)"
      , countQuery = "select count(e) from NotificationEntity e"
      + " where e.deleted = 0 "
      + " and (e.id = :#{#dto.id} or :#{#dto.id} is null) "
      + " and (e.content like :#{#dto.content} or :#{#dto.content} is null)"
      + " and (e.isRead = :#{#dto.isRead} or :#{#dto.isRead} is null)"
      + " and (e.role = :#{#dto.role} or :#{#dto.role} is null)"
      + " and (e.ownerId = :#{#dto.ownerId} or :#{#dto.ownerId} is null)")
  Page<NotificationEntity> search(NotificationDTO dto, Pageable page);

  @Override
  @Transactional
  @Modifying
  @Query(value = "update NotificationEntity e set e.deleted = 1 "
      + " where e.id = ?#{#id} ")
  void deleteById(Long id);

  @Query("select count(e) from NotificationEntity e"
      + " where e.deleted = 0 and e.isRead = 0")
  Integer countRead();
}
