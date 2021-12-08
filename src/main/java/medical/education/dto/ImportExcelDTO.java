package medical.education.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ImportExcelDTO {

  private MultipartFile file;
  private int sheet;
  private int no;
}
