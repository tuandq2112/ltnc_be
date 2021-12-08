package medical.education.dao.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.education.enums.RegisterEnum;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "register")
@Where(clause = "deleted=0")
public class RegisterEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * sinh viên đăng ký
   */
  @Column(name = "student_id")
  private Long studentId;

  @OneToOne
  @JoinColumn(name = "student_id",insertable = false,updatable = false)
  private UserEntity student;

  /**
   * khóa đăng ký
   */
  @Column(name = "course_id")
  private Long courseId;

    @ManyToOne
    @JoinColumn(name = "course_id",insertable = false,updatable = false)
    private CourseEntity course;

  /**
   * danh sách kết quả
   */
  @OneToMany(mappedBy = "register")
  private List<ResultEntity> results;

  /**
   * Trạng thái học tập
   * 0 đang học
   * 1 hoàn  thành
   * 2 trượt
   */
  @JoinColumn(name = "status")
  private RegisterEnum status;

  /**
   * tổng kết
   */
  private Double total;

  /**
   * xếp loại học tập
   */
  @Column(length = 12)
  private String kind;

  /**
   * chữ ký
   */
  private String signature;

  /**
   * Kỳ học:
   * = năm khai giảng + tháng khai giảng
   */
  private Integer semester;
}
