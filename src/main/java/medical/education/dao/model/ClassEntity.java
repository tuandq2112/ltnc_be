package medical.education.dao.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.jpa.repository.Query;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ClassEntity implements Serializable {
  @Id
  private Long id;

  private String name;

  private Double midPoint;

  private Double endPoint;

  private Double total;

  private Integer muster;

  private Long classId;

  private Long subjectId;

  private Integer lesson;

  private Long resultId;
}
