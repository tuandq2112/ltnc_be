package medical.education.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import medical.education.dao.model.DashboardThongKeSLDKEntity;
import medical.education.dao.model.ThongKeDiemTheoKhoaEntity;
import medical.education.dao.repository.DashboardThongKeSLDKRepository;
import medical.education.dto.ThongKeDiemTheoKhoaDTO;
import medical.education.dto.ThongKeDiemTheoKhoaDTO.DiemMonHoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardThongKeSLDKServiceImpl implements DashboardThongKeSLDKService {

    @Autowired
    private DashboardThongKeSLDKRepository repository;

    @Override
    public List<DashboardThongKeSLDKEntity> thongKeSoLuongDangKy(Integer namKhaoSat) {
        List<DashboardThongKeSLDKEntity> dashboardTk = new ArrayList<>();

        List<DashboardThongKeSLDKEntity> dashboard = repository.thongKeTheoNam(namKhaoSat);

        int[] a = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        for (DashboardThongKeSLDKEntity e : dashboard) {
            a[e.getId() - 1] = 1;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 0) {
                DashboardThongKeSLDKEntity entity = new DashboardThongKeSLDKEntity();
                entity.setId(i + 1);
                entity.setMonth(i + 1);
                entity.setYear(Calendar.getInstance().get(Calendar.YEAR));
                entity.setTotal(0l);
                dashboardTk.add(entity);
            } else {
                for (DashboardThongKeSLDKEntity e : dashboard) {
                    if (e.getMonth() == i + 1) {
                        dashboardTk.add(e);
                    }
                }
            }
        }
        return dashboardTk;
    }

    @Override
    public List<ThongKeDiemTheoKhoaDTO> thongKeDiemTheoKhoa(Long idKhoa) {
        List<ThongKeDiemTheoKhoaEntity> thongKeDiemTheoKhoaEntities = repository
                .thongKeDiemTheoKhoa(idKhoa);
        List<ThongKeDiemTheoKhoaDTO> thongKeDiemTheoKhoaDTOS = new ArrayList<>();
        for (ThongKeDiemTheoKhoaEntity e : thongKeDiemTheoKhoaEntities) {
            ThongKeDiemTheoKhoaDTO dto = new ThongKeDiemTheoKhoaDTO();
            dto.setId(e.getId());
            dto.setName(e.getName());
            List<String> listSubject = new ArrayList<>(
                    Arrays.asList(e.getSubject().split(",")));

            List<String> listPass = new ArrayList<>(Arrays.asList(e.getPass().split(",")));
            List<String> listTotal = new ArrayList<>(Arrays.asList(e.getTotal().split(",")));
            List<String> listId = new ArrayList<>(Arrays.asList(e.getIds().split(",")));
            List<DiemMonHoc> diemMonHocList = new ArrayList<>();
            if(listSubject.size()==listPass.size()&&listPass.size()==listTotal.size()){
                for(int i = 0; i < listSubject.size();i++){
                    DiemMonHoc diemMonHoc = new DiemMonHoc();
                    diemMonHoc.setPass(listPass.get(i));
                    diemMonHoc.setSubject(listSubject.get(i));
                    diemMonHoc.setTotal(listTotal.get(i));
                    diemMonHoc.setId(listId.get(i));
                    diemMonHocList.add(diemMonHoc);
                }
            }
            dto.setListDiem(diemMonHocList);
            thongKeDiemTheoKhoaDTOS.add(dto);

        }
        return thongKeDiemTheoKhoaDTOS;
    }
}
