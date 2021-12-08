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
                name = "thongKeSoLuongDangKy",
                procedureName = "thong_ke_so_luong_dang_ky",
                resultClasses = {DashboardThongKeSLDKEntity.class},
                parameters = {
                        @StoredProcedureParameter(
                                name = "nam_khao_sat",
                                type = Integer.class,
                                mode = ParameterMode.IN)
                })
})
@Setter
@Getter
@NoArgsConstructor
public class DashboardThongKeSLDKEntity {

    @Id
    private Integer id;

    private Integer month;

    private Integer year;

    private Long total;
}
