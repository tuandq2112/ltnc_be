package medical.education.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailDTO {
    private String toEmail;
    private String cc;
    private String context;
    private String subject;
}
