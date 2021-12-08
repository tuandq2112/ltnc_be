package medical.education.controller;

import medical.education.dto.SendEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.dto.ResponseEntity;
import spring.backend.library.service.MailService;

@RestController
@RequestMapping("/email")
public class EmailController {

  @Autowired
  private MailService mailService;

  @PostMapping
  public ResponseEntity sendEmail(@RequestBody SendEmailDTO dto) {
    mailService
        .sendSimpleEmail(dto.getToEmail(), dto.getSubject(), dto.getContext(), dto.getCc());
    return new ResponseEntity(HttpStatus.OK);
  }
}
