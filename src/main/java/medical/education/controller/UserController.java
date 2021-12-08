package medical.education.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import medical.education.dto.ImportExcelDTO;
import medical.education.dto.LoginDTO;
import medical.education.dto.UserDTO;
import medical.education.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.library.controller.BaseController;
import spring.backend.library.dto.ResponseEntity;
import spring.backend.library.exception.BaseException;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<UserDTO, UserService> {

  @Autowired
  private UserService service;

  @Override
  public UserService getService() {
    return service;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginDTO dto) {
    return response(service.validateLogin(dto));
  }

  @GetMapping("/profile")
  public ResponseEntity profile() {
    return response(service.profile());
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody UserDTO userDTO) {
    return response(getService().register(userDTO));
  }

  @PutMapping("/change-password")
  public ResponseEntity changePassword(@RequestBody UserDTO userDTO) {
    return response(getService().changePassword(userDTO));
  }

  @PutMapping("/reset-password")
  public ResponseEntity resetPassword(@RequestBody UserDTO userDTO) {
    return response(getService().resetPassword(userDTO));
  }

  @RequestMapping(value = "/upload-avatar", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity uploadAvatar(@RequestParam MultipartFile file) {
    return response(getService().uploadAvatar(file));
  }

  @RequestMapping(value = "/upload-avatar/{id}", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity avatar(@PathVariable(name = "id") Long id,@RequestParam MultipartFile file) {
    return response(getService().uploadAvatar(file,id));
  }

  @PutMapping(value = "/change-info")
  public ResponseEntity changeInfo(@RequestBody UserDTO userDTO) {
    return response(getService().changeInfo(userDTO));
  }

  @PutMapping(value = "/admin-approve-change-info/{id}")
  public ResponseEntity approve(@PathVariable Long id) {
    return response(getService().adminApproveChange(id));
  }

  @PutMapping(value = "/admin-approve-teacher/{id}")
  public ResponseEntity approveTeacher(@PathVariable Long id, @RequestBody UserDTO dto) {
    return response(getService().approveTeacher(id, dto));
  }

  @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = {
      "multipart/form-data"})
  public ResponseEntity importExcel(ImportExcelDTO dto) {
    return response(getService().importData(dto.getFile(), dto.getSheet(), dto.getNo()));
  }

}
