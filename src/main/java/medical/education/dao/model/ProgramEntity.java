package medical.education.dao.model;

import io.swagger.models.auth.In;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "program")
@Where(clause = "deleted = 0")
public class ProgramEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String code;

  @Transient
  private Integer lesson;

  /**
   * khóa học hiện tại
   */
  @Column(name = "current_course_id")
  private Long currentCourseId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "current_course_id", insertable = false, updatable = false)
  private CourseEntity currentCourse;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name = "program_subject",
      joinColumns = @JoinColumn(name = "program_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
  private List<SubjectEntity> subjects;

  @OneToMany(mappedBy = "programEntity")
  private List<CourseEntity> courseEntities;
}
