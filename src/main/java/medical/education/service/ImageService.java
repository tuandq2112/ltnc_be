package medical.education.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;


public interface ImageService {

  ResponseEntity<byte[]> getImage(String fileName, HttpServletRequest httpServletRequest)
      throws IOException;

}
