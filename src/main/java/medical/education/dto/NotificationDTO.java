package medical.education.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.education.enums.NotificationEnum;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO extends BaseDTO {
  private Long id;
  private String content;
  private NotificationEnum isRead;
  private Long ownerId;
  private Short role;
}
