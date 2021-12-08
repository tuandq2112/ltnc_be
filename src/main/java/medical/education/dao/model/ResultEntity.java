package medical.education.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "results")
@Where(clause = "deleted=0")
public class ResultEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * sinh viên
   */
  @Column(name = "student_id")
  private Long studentId;

  @ManyToOne
  @JoinColumn(name = "student_id",updatable = false,insertable = false)
  private UserEntity student;

  /**
   * khóa học
   */
  @Column(name = "course_id")
  private Long courseId;

  @OneToOne
  @JoinColumn(name = "course_id",insertable = false,updatable = false)
  private CourseEntity course;

  /**
   * môn học
   */
  @Column(name = "subject_id")
  private Long subjectId;

  @OneToOne
  @JoinColumn(name = "subject_id",updatable = false,insertable = false)
  private SubjectEntity subject;

  /**
   * đăng ký
   */
  @Column(name = "register_id")
  private Long registerId;

  @ManyToOne()
  @JoinColumn(name = "register_id",updatable = false,insertable = false)
  private RegisterEntity register;

  /**
   * điểm danh
   */
  @Column(name = "attendance")
  private Integer muster;

  /**
   * điểm giữa kỳ
   */
  private Double midPoint;

  /**
   * điểm cuối kỳ
   */
  private Double endPoint;

  /**
   * tổng kết môn
   */
  private double total;

  private boolean isPass;

}
