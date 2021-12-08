package medical.education.controller;

import medical.education.dto.KsHocVienDTO;
import medical.education.service.KsHocVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;

@RestController
@RequestMapping("/ks-hoc-vien")
public class KsHocVienController extends BaseController<KsHocVienDTO, KsHocVienService> {

    @Autowired
    KsHocVienService service;

    @Override
    public KsHocVienService getService() {
        return service;
    }
}
