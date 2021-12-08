package medical.education.dto;

import lombok.ToString;
import spring.backend.library.dto.BaseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class PlaceDTO extends BaseDTO {
  private String address;
  private Long healthFacilityId;
  private Object healthFacility;
}
