package medical.education.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.dao.model.ScheduleEntity;
import medical.education.enums.KipHocEnum;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ScheduleDTO extends BaseDTO {

  private Short day;
  private Long placeId;
  private Object placeInfo;
  private Long subjectId;
  private Object subjectInfo;
  private Long teacherId;
  private String dates;
  private Object teacher;
  private Long courseId;
//  private Object courseInfo;
  private String nameCourse;
  private String codeCourse;
  private Short kipHoc;
  private Integer status;
  private Long changeScheduleId;
  private Object changeInfo;
  private String reason;
  private Long deleteId;
  private Integer numberRegister;
}
