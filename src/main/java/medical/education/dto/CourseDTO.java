package medical.education.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.enums.CourseStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class CourseDTO extends BaseDTO {

  private Long id;

  private String name;

  private String code;

  private String nameHealthFacility;

  private String nameUserCreated;

  private Short status;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date ngayKhaiGiang;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date ngayKetThuc;

  private Integer semester;

  private Long price;

  private Integer numberLesson;

  private Integer numberRegister;

  private Integer limitRegister;

  private Long healthFacilityId;

  private Object healthFacility;

  private String subjectIds;

  private List<SubjectDTO> listSubject;

  private List<UserDTO> listRegisters;

  private Object userCreated;

  private List<ScheduleDTO> listSchedules;

  private CourseStatusEnum courseStatusEnum;

  private Long programId;

  private ProgramDTO programInfo;

  private Integer scheduled = 0;

  private String date;
}
