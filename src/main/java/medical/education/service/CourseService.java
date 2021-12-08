package medical.education.service;

import spring.backend.library.service.BaseService;
import medical.education.dto.CourseDTO;

public interface CourseService extends BaseService<CourseDTO> {

    void synchronizedData();

}
