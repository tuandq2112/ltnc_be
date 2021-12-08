package medical.education.controller;

import medical.education.dto.ClassDTO;
import medical.education.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseResponseController;
import spring.backend.library.dto.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/class")
public class ClassController extends BaseResponseController {

  @Autowired
  private ClassService classService;

  @GetMapping
  public ResponseEntity search(ClassDTO dto, Pageable page) {
    return response(classService.search(dto,page));
  }
}
