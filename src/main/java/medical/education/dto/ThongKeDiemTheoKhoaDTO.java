package medical.education.dto;

import java.util.List;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ThongKeDiemTheoKhoaDTO {

    private Long id;
    private String name;
    private List<DiemMonHoc> listDiem;

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class DiemMonHoc {
        private String subject;
        private String pass;
        private String total;
        private String id;
    }
}
