package medical.education.service;

import medical.education.dao.model.KsHocVienEntity;
import medical.education.dao.repository.KsHocVienRepository;
import medical.education.dto.KsHocVienDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.backend.library.service.AbstractBaseService;

@Service
public class KsHocVienServiceImpl extends
        AbstractBaseService<KsHocVienEntity, KsHocVienDTO, KsHocVienRepository> implements
        KsHocVienService {

    @Autowired
    private KsHocVienRepository repository;

    @Override
    protected KsHocVienRepository getRepository() {
        return repository;
    }
}
