package medical.education.controller;

import medical.education.dto.ProgramDTO;
import medical.education.service.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;

@RestController
@RequestMapping("/program")
public class ProgramController extends BaseController<ProgramDTO, ProgramService> {

  @Autowired
  private ProgramService programService;

  @Override
  public ProgramService getService() {
    return programService;
  }
}
