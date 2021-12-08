package medical.education.dao.model;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import medical.education.enums.CourseStatusEnum;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Entity
@Table(name = "course")
@Where(clause = "deleted=0")
@Getter
@Setter
@NoArgsConstructor
public class CourseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //  @Column(unique = true)
    private String code;

    /**
     * 1: Thời gian đăng ký 2: đang học 3: hoàn thành
     */
    private Short status;

//  private CourseStatusEnum courseStatusEnum;

    /**
     * thời gian bắt đầu
     */
    private Date ngayKhaiGiang;

    /**
     * thời gian kết thúc
     */
    private Date ngayKetThuc;

    /**
     * Kỳ học = năm khai giảng + tháng khai giảng + ngày khai giảng
     */
    private Integer semester;

    /**
     * giá
     */
    private Long price;

    /**
     * số tiết học
     */
    private Integer numberLesson;

    /**
     * giới hạn đăng ký
     */
    private Integer limitRegister;

    /**
     * giới hạn tối thiểu cho phép đăng kí
     */
    private Integer minRegister;

    /**
     * cơ sở y tế
     */
    @Column(name = "health_facility_id")
    private Long healthFacilityId;

    @OneToOne
    @JoinColumn(name = "health_facility_id", insertable = false, updatable = false)
    private HealthFacilityEntity healthFacility;

    @Column(name = "programId")
    private Long programId;

//  /**
//   * các môn trong chương trình
//   */
//  private String subjectIds;

//  @ManyToMany(fetch = FetchType.LAZY)
//  @JoinTable(
//      name = "course_subject",
//      joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
//      inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
//  private List<SubjectEntity> subjects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<ScheduleEntity> schedules;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<RegisterEntity> registerEntities;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "programId", insertable = false, updatable = false)
    private ProgramEntity programEntity;
}
