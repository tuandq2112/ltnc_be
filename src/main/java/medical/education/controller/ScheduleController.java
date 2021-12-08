package medical.education.controller;

import medical.education.dto.ScheduleDTO;
import medical.education.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;
import spring.backend.library.dto.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/schedule")
public class ScheduleController extends BaseController<ScheduleDTO, ScheduleService> {

  @Autowired
  private ScheduleService scheduleService;

  @Override
  public ScheduleService getService() {
    return scheduleService;
  }

  @GetMapping("/get")
  public ResponseEntity get() {
    return response(getService().getSchedule());
  }

  @GetMapping("/get-class")
  public ResponseEntity getListClass(ScheduleDTO dto, Pageable page) {
    return response(scheduleService.getListClass(dto, page));
  }

//  @GetMapping("/get-busy")
//  public ResponseEntity getScheduleBusy(ScheduleDTO dto, Pageable page) {
//    return response(scheduleService.getSchedulebBusy());
//  }

  @PutMapping("/handle-change-schedule/{id}")
  public ResponseEntity handleChange(@PathVariable(name = "id") Long id,@RequestBody ScheduleDTO dto){
    return response(scheduleService.handleChangeSchedule(id,dto));
  }

  @GetMapping("/count-change-schedule")
  public ResponseEntity count(){
    return response(scheduleService.countChange());
  }

  @GetMapping("/find-all-change-schedule")
  public ResponseEntity findAllChange(Pageable page){
    return response(scheduleService.findAllChange(page));
  }

  @PostMapping("/change-schedule/{id}")
  public ResponseEntity changeSchedule(@PathVariable(name = "id") Long id,@RequestBody ScheduleDTO dto){
    return response(scheduleService.changeSchedule(id,dto));
  }

}
