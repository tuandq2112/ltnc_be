package medical.education.dao.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Entity
@Table(name = "subject")
@Where(clause = "deleted=0")
@Getter
@Setter
@NoArgsConstructor
public class SubjectEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    /**
     * tên môn học
     */
    @Column(nullable = false)
    private String name;

    /**
     * tên viết tắt
     */
    private String shortName;

    /**
     * loại môn học
     */
//  @Column(columnDefinition = "VARCHAR(60) CHECK (status in ('LT + BT','TH'))", nullable = false)
    private String type;

    /**
     * số tiết
     */
    private Integer lesson;

    /**
     * giá
     */
    private Long price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_subject",
            joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    private List<CourseEntity> courseEntities;

    private Long midtermScore;

    private Long finalScore;

    private Boolean isUsePoint;

}