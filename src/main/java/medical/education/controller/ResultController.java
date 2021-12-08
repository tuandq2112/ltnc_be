package medical.education.controller;

import medical.education.dto.ResultDTO;
import medical.education.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;
import spring.backend.library.dto.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/result")
public class ResultController extends BaseController<ResultDTO, ResultService> {

  @Autowired
  private ResultService resultService;

  @Override
  public ResultService getService() {
    return resultService;
  }

  @PutMapping("/attendance/{id}")
  public ResponseEntity enterPoint(@PathVariable("id") Long id) {
    return response(getService().attendance(id));
  }

  @GetMapping("/get-result")
  public ResponseEntity getResult(ResultDTO searchDTO, Pageable page) {
    return response(getService().getResult(searchDTO, page));
  }
}
