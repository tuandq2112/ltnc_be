package medical.education.service;

import medical.education.dto.ScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import spring.backend.library.service.BaseService;

public interface ScheduleService extends BaseService<ScheduleDTO> {

  @PreAuthorize("hasAnyRole('ADMIN','STUDENT','TEACHER')")
  Page<ScheduleDTO> getSchedule();

  Page<ScheduleDTO> getListClass(ScheduleDTO dto, Pageable page);

//  public Page<ScheduleDTO> getSchedulebBusy();

  public ScheduleDTO handleChangeSchedule(Long id,ScheduleDTO dto);

  public Integer countChange();

  public Page<ScheduleDTO> findAllChange(Pageable page);

  public ScheduleDTO changeSchedule(Long id,ScheduleDTO dto);
}
