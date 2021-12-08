package medical.education.service;

import java.util.List;
import medical.education.dao.model.ClassEntity;
import medical.education.dao.repository.ClassRepository;
import medical.education.dto.ClassDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
  @Autowired
  private ClassRepository classRepository;

  public Page<ClassEntity> search(ClassDTO dto, Pageable pageable){
    return classRepository.search(dto,pageable);
  }
}
