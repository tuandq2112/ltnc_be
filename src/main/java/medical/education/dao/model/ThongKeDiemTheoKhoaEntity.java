package medical.education.dao.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("")
@Immutable
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "thongKeDiemTheoKhoa",
                procedureName = "thong_ke_diem_theo_khoa",
                resultClasses = {ThongKeDiemTheoKhoaEntity.class},
                parameters = {
                        @StoredProcedureParameter(
                                name = "id_khoa",
                                type = Long.class,
                                mode = ParameterMode.IN)
                })
})
@Setter
@Getter
@NoArgsConstructor
public class ThongKeDiemTheoKhoaEntity {

    @Id
    private Long id;
    private String name;
    private String subject;
    private String pass;
    private String total;
    private String ids;
}
