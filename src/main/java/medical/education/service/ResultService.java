package medical.education.service;

import medical.education.dto.ResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.backend.library.service.BaseService;

public interface ResultService extends BaseService<ResultDTO> {
  void generateResultsForStudent(Long classId,Long studentId,Long registerId);

  // teacher attendance
  ResultDTO attendance(Long id);

  // student get result
  Page<ResultDTO> getResult(ResultDTO searchDTO,Pageable page);
}
