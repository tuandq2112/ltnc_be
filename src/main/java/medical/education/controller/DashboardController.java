package medical.education.controller;

import medical.education.service.DashboardThongKeSLDKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseResponseController;
import spring.backend.library.dto.ResponseEntity;

@CrossOrigin(allowedHeaders = "*")
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseResponseController {

    @Autowired
    private DashboardThongKeSLDKService service;

    @GetMapping("/thong-ke-so-luong-dang-ky")
    @Transactional
    public ResponseEntity thongKeSoLuong(@RequestParam Integer namKhaoSat) {
        return response(service.thongKeSoLuongDangKy(namKhaoSat));
    }

    @GetMapping("/thong-ke-diem-theo-khoa")
    @Transactional
    public ResponseEntity thongKeDoem(@RequestParam Long idKhoa) {
        return response(service.thongKeDiemTheoKhoa(idKhoa));
    }
}
