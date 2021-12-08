package medical.education.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.enums.RegisterEnum;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class RegisterDTO extends BaseDTO {

  private Long studentId;

  private Long courseId;

  private Object studentInfo;

  private Object courseInfo;

  private RegisterEnum status;

  private Double total;

  private String kind;

  private Integer semester;

  private Object programInfo;
}
