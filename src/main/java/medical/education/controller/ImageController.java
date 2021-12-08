package medical.education.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import medical.education.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RequestMapping("/images")
@RestController
public class ImageController{
  @Autowired
  private ImageService imageService;

  @GetMapping("/{fileName:.*}")
  public ResponseEntity<byte[]> getImage(@PathVariable String fileName, HttpServletRequest http)
      throws IOException {
    return imageService.getImage(fileName,http);
  }
}
