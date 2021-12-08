package medical.education.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class SubjectDTO extends BaseDTO {

  private String code;

  private String name;

  private String type;

  private Integer lesson;

  private Long price;

  private Boolean hasScheduled = false;

  private Long midtermScore;

  private Long finalScore;

  private Boolean isUsePoint;
}
