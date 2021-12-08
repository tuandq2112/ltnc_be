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
public class ResultDTO extends BaseDTO {

  private Long studentId;

  private Object studentInfo;
  /**
   * lớp học
   */
  private Long courseId;

  private Long registerId;

  private Integer semester;

  private Object courseInfo;

  private Long subjectId;

  private Object subjectInfo;

  private Integer muster;

  private Double midPoint;

  private Double endPoint;

  private Double total;

  private String rank;

  private Boolean isPass;

}
