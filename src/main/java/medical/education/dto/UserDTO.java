package medical.education.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.enums.GenderEnum;
import medical.education.enums.RoleEnum;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class UserDTO extends BaseDTO {
  private String avatar;

  private String username;

  private String password;

  private String passwordChange;

  private String fullName;

  private String value;

  private Short status;

  private Long age;

  private String cmnd;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date birth;

  private GenderEnum gender;

  private String address;

  private String email;

  private String phoneNumber;

  private Long idChange;

  private RoleEnum role;

  private List<ResultDTO> listStudyProcess;

  private List<CourseDTO> courses;

  private Long currentCourseId;

  private CourseDTO currentCourse;

  private List<Long> subjectsId;

  private Long subjectId;

  private List<SubjectDTO> subjects;

  private String userCreated;

  private String userUpdated;
}
