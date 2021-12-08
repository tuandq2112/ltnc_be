package medical.education.dao.repository;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import medical.education.dao.model.DashboardThongKeSLDKEntity;
import medical.education.dao.model.ThongKeDiemTheoKhoaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardThongKeSLDKRepository {

    @Autowired
    @Qualifier("entityManagerFactory")
    private EntityManager firstDataManager;

    public List<DashboardThongKeSLDKEntity> thongKeTheoNam(Integer namKhaoSat) {

        List<DashboardThongKeSLDKEntity> dtos = firstDataManager
                .createNamedStoredProcedureQuery("thongKeSoLuongDangKy")
                .setParameter("nam_khao_sat", namKhaoSat)
                .getResultList();

        return dtos;
    }

    public List<ThongKeDiemTheoKhoaEntity> thongKeDiemTheoKhoa(Long idKhoa) {
        List<ThongKeDiemTheoKhoaEntity> dtos = firstDataManager
                .createNamedStoredProcedureQuery("thongKeDiemTheoKhoa"
                ).setParameter("id_khoa", idKhoa).getResultList();
        return dtos;
    }
}
