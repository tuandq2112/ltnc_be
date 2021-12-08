package medical.education.service;

import medical.education.dto.NotificationDTO;
import spring.backend.library.service.BaseService;

public interface NotificationService extends BaseService<NotificationDTO> {

  NotificationDTO read(Long id);

  Integer countRead();
}