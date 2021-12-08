package medical.education.service;

import java.util.List;
import medical.education.dto.RegisterDTO;
import spring.backend.library.service.BaseService;

public interface RegisterService extends BaseService<RegisterDTO> {

  void changeSemester(Long courseId, Integer semester);

  List<Integer> getListSemester(Long courseId);

  void synchronizedData();
}
