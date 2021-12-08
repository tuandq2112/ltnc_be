package medical.education.service;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import medical.education.dao.model.CourseEntity;
import medical.education.dao.model.ProgramEntity;
import medical.education.dao.model.SubjectEntity;
import medical.education.dao.repository.CourseRepository;
import medical.education.dao.repository.ProgramRepository;
import medical.education.dao.repository.SubjectRepository;
import medical.education.dto.CourseDTO;
import medical.education.dto.ProgramDTO;
import medical.education.dto.SubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.service.AbstractBaseService;

@Service
public class ProgramServiceImpl extends
    AbstractBaseService<ProgramEntity, ProgramDTO, ProgramRepository> implements
    ProgramService {

  @Autowired
  private ProgramRepository programRepository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private SubjectService subjectService;

  @Autowired
  private CourseRepository courseRepository;

  @Override
  protected ProgramRepository getRepository() {
    return programRepository;
  }

  @Override
  protected void beforeSave(ProgramEntity entity, ProgramDTO dto) {
    super.beforeSave(entity, dto);
    if (getRepository().existsByCodeAndId(entity.getCode(), entity.getId())) {
      throw new BaseException(410, "Mã chương trình đã tồn tại");
    }
    if (!Strings.isNullOrEmpty(dto.getSubjectIds())) {
      List<SubjectEntity> listSubject = new ArrayList<>();
      String[] ids = dto.getSubjectIds().substring(1, dto.getSubjectIds().length() - 1)
          .split(",");
      for (String idStr : ids) {
        Long id = Long.valueOf(idStr);
        SubjectEntity e = subjectRepository.findById(id).get();
        listSubject.add(e);
      }
      entity.setSubjects(listSubject);
    }
  }

  @Override
  public Page<ProgramDTO> search(ProgramDTO dto, Pageable pageable) {
    if (!Strings.isNullOrEmpty(dto.getName())) {
      dto.setName("%" + dto.getName().toLowerCase().trim().replaceAll(" ", "%") + "%");
    }
    if (!Strings.isNullOrEmpty(dto.getCode())) {
      dto.setCode("%" + dto.getCode().toLowerCase().trim().replaceAll(" ", "%") + "%");
    }

    return super.search(dto, pageable);
  }

  @Override
  protected void specificMapToDTO(ProgramEntity entity, ProgramDTO dto) {
    super.specificMapToDTO(entity, dto);
    List<SubjectDTO> listSubjects = new ArrayList<>();
    long price = 0;
    int totalLesson = 0;
    for (SubjectEntity e : entity.getSubjects()) {
      price += e.getPrice();
      totalLesson += e.getLesson();
      listSubjects.add(subjectService.findById(e.getId()));
    }
    dto.setNumberTurn(courseRepository.countByProgramId(entity.getId()));
    dto.setPrice(price);
    dto.setLesson(totalLesson);
    dto.setListSubjects(listSubjects);
    dto.setTotNghiep(getRepository().getTongSoTotNghiep(entity.getId()));
    dto.setTruot((getRepository().getTongSoTruot(entity.getId())));
//    if (entity.getSubjects() != null) {
//      dto.setListSubjects(
//          entity.getSubjects().stream().map(e -> subjectService.findById(e.getId())).collect(
//              Collectors.toList()));
//
//    }
  }

  @Override
  protected void beforeDelete(Long id) {
    super.beforeDelete(id);
    List<CourseEntity> courseEntities = courseRepository.findByProgramId(id).orElse(null);
    if (courseEntities != null) {
      courseEntities.forEach(e -> {
        e.setProgramId(null);
        courseRepository.save(e);
      });
    }
  }
}
