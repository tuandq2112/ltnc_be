package medical.education.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class ClassDTO implements Serializable {
  private Long id;

  private String name = "";

  private Double midPoint;

  private Double endPoint;

  private Double total;

  private String muster;

  private Long courseId = -1L;

  private Long subjectId = -1L;

  private Integer lesson;

}
