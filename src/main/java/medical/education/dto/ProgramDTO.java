package medical.education.dto;

import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.dao.model.SubjectEntity;
import spring.backend.library.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ProgramDTO extends BaseDTO {


  private String name;

  private String code;

  private String subjectIds;

  private List<Long> subjectsIds;

  private List<SubjectDTO> listSubjects;

  private Integer numberTurn;

  private Long price;

  private Integer done;

  private Integer fail;

  private Integer lesson;

  private Object currentCourse;

  private Long totNghiep;

  private Long truot;
}
