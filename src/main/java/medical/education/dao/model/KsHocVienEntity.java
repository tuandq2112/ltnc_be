package medical.education.dao.model;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import medical.education.enums.GenderEnum;
import org.hibernate.annotations.Where;
import spring.backend.library.dao.model.BaseEntity;

@Entity
@Table(name = "ks_hoc_vien")
@Where(clause = "deleted = 0")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class KsHocVienEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tenBenhVien;

  private ZonedDateTime ngayDienPhieu;

  private GenderEnum gender;

  private Integer age;

  private String soDiDong;

  private String khoangCach;

  private Integer a1;

  private Integer a2;

  private Integer a3;

  private Integer a4;

  private Integer a5;

  private Integer b1;

  private Integer b2;

  private Integer b3;

  private Integer b4;

  private Integer b5;

  private Integer b6;

  private Integer b7;

  private Integer b8;

  private Integer b9;

  private Integer b10;

  private Integer c1;

  private Integer c2;

  private Integer c3;

  private Integer c4;

  private Integer c5;

  private Integer c6;

  private Integer c7;

  private Integer c8;

  private Integer d1;

  private String d1ChiTiet;

  private Integer d2;

  private String d2ChiTiet;

  private Integer d3;

  private String d3ChiTiet;

  private Integer d4;

  private String d4ChiTiet;

  private Integer e1;

  private Integer e2;

  private Integer e3;

  private Integer e4;

  private Integer f;

  private Integer g;

  private String gKhac;

}
