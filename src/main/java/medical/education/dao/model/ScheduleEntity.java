package medical.education.dao.model;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
import medical.education.enums.KipHocEnum;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "schedule")
@Where(clause = "deleted=0")
public class ScheduleEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * thứ
   */
  private Short day;

  private Short kipHoc;

  /**
   * địa điểm
   */
  @Column(name = "place_id")
  private Long placeId;

  @ManyToOne()
  @JoinColumn(name = "place_id", insertable = false, updatable = false)
  private PlaceEntity place;

  /**
   * môn học
   */
  @Column(name = "subject_id")
  private Long subjectId;

  @OneToOne
  @JoinColumn(name = "subject_id", insertable = false, updatable = false)
  private SubjectEntity subject;

  /**
   * 0 bản ghi thường
   * 1 bản ghi giả chứa thông tin thay đổi
   */
  @Column(columnDefinition = "tinyint default 0")
  private short status = 0;

  /**
   * khóa học
   */
  @Column(name = "course_id")
  private Long courseId;

  @ManyToOne
  @JoinColumn(name = "course_id", updatable = false, insertable = false)
  private CourseEntity course;

  /**
   * giảng viên
   */
  @Column(name = "teacher_id")
  private Long teacherId;

  @Column(length = 1000)
  private String dates;

  @ManyToOne
  @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
  private UserEntity teacher;

  /**
   * thay đổi lịch
   */
  @Column(name = "change_schedule_id")
  private Long changeScheduleId;

  @OneToOne
  @JoinColumn(name = "change_schedule_id",updatable = false,insertable = false)
  private ScheduleEntity changeInformation;

//  @OneToOne(mappedBy = "changeSchedule")
//  private ScheduleEntity changeInformation;

  /**
   * lý do hủy lịch
   */
  private String reason;
}
