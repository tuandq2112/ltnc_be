package medical.education.service;

import java.util.List;
import medical.education.dao.model.DashboardThongKeSLDKEntity;
import medical.education.dto.ThongKeDiemTheoKhoaDTO;

public interface DashboardThongKeSLDKService {

    List<DashboardThongKeSLDKEntity> thongKeSoLuongDangKy(Integer namKhaoSat);

    List<ThongKeDiemTheoKhoaDTO> thongKeDiemTheoKhoa(Long idKhoa);
}
