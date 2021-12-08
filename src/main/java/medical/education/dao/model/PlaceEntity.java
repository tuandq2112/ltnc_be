package medical.education.dao.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "place")
@Where(clause = "deleted = 0")
public class  PlaceEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * địa chỉ
   */
  @Column(unique = true)
  private String address;

  /**
   * cơ sở y tế
   */
  @Column(name = "health_facility_id")
  private Long healthFacilityId;

  @ManyToOne
  @JoinColumn(name = "health_facility_id", updatable = false, insertable = false)
  private HealthFacilityEntity healthFacility;
}
