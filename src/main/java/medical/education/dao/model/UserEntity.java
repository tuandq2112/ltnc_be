package medical.education.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.education.enums.GenderEnum;
import medical.education.enums.RoleEnum;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Entity
@Table(name = "users")
@Where(clause = "deleted=0")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String avatar;

  private String username;

  private String password;

  private String fullName;

  private String cmnd;

  private Date birth;

  private String value;

  private Long age;

  private GenderEnum gender;

  private String address;

  private String email;

  private String phoneNumber;

  private Long idChange;

  /**
   * 0: hoạt động
   * 1: khóa
   */
  private Short status;

  /**
   * 1: admin
   * 2: teacher
   * 3: student
   */
  private RoleEnum role;

  /**
   * KHóa đang học hiện tại
   */
  @JoinColumn(name = "current_course")
  private Long currentCourseId;

  @OneToOne
  @JoinColumn(name = "current_course",updatable = false,insertable = false)
  private CourseEntity currentCourse;

//  @Column(name = "role_id")
//  private Long roleId;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "role_id",updatable = false,insertable = false)
//  private RoleEntity roleEntity;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "course_user",
      joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
  private List<CourseEntity> courses;

  @OneToMany(mappedBy = "student")
  private List<ResultEntity> listStudyProcess;

  @ManyToMany
  @JoinTable(name = "user_subject",
  joinColumns = @JoinColumn(name = "teacher_id",referencedColumnName = "id"),
  inverseJoinColumns = @JoinColumn(name = "subject_id",referencedColumnName = "id"))
  private List<SubjectEntity> subjects;
}
