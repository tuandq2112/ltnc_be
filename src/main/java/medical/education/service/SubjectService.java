package medical.education.service;

import java.util.List;
import medical.education.dto.SubjectDTO;
import spring.backend.library.service.BaseService;

public interface SubjectService extends BaseService<SubjectDTO> {

    List<String> getDistinctSubject();
}
