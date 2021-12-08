package medical.education.service;

import io.jsonwebtoken.Jwts;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import medical.education.dao.model.SubjectEntity;
import medical.education.dao.model.UserEntity;
import medical.education.dao.repository.SubjectRepository;
import medical.education.dao.repository.UserRepository;
import medical.education.dto.LoginDTO;
import medical.education.dto.UserDTO;
import medical.education.enums.GenderEnum;
import medical.education.enums.RoleEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import spring.backend.library.config.filter.JwtProvider;
import spring.backend.library.config.filter.JwtProvider.JwtTokenProperties;
import spring.backend.library.config.userdetail.UserDetail;
import spring.backend.library.dto.ResponseEntity;
import spring.backend.library.exception.BaseException;
import spring.backend.library.msg.Message;
import spring.backend.library.service.AbstractBaseService;
import spring.backend.library.storage.StorageService;
import spring.backend.library.utils.DigestUtil;
import spring.backend.library.utils.ExcelUtil;

@Service
public class UserServiceImpl extends
    AbstractBaseService<UserEntity, UserDTO, UserRepository> implements UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private StorageService storageService;

  @Override
  protected UserRepository getRepository() {
    return repository;
  }

  @Override
  public Map<String, Object> validateLogin(LoginDTO dto) {
    if (dto.getPassword() == null || dto.getUsername() == null) {
      throw new BaseException(400, "password.or.username.is.null");
    }
    UserEntity userEntity = repository.findByUsername(dto.getUsername()).orElse(null);

    if (userEntity == null) {
      throw new BaseException(400, "username.not.exists");
    }

    if (!DigestUtil.sha256Hex(dto.getPassword()).equals(userEntity.getPassword())) {
      throw new BaseException(400, "password.invalid");
    }

    if (userEntity.getStatus().intValue() != 0) {
      throw new BaseException(405, "account is invalid");
    }

    List<String> roles = new ArrayList<>();

    String role = "";
    if (userEntity.getRole().getName() != null) {
      role = userEntity.getRole().getName();
    }

    if (role != null) {
      roles.add("ROLE_" + role);
    }

    JwtTokenProperties jwts = JwtTokenProperties.builder()
        .id(userEntity.getId())
        .username(userEntity.getUsername())
        .fullName(userEntity.getFullName())
        .role(roles.get(0))
        .avatar(userEntity.getAvatar())
        .privileges(roles)
        .build();
    return jwtProvider.generateToken(jwts);
  }

  @Override
//  @PreAuthorize("hasAnyRole('ADMIN')")
  protected void beforeSave(UserEntity entity, UserDTO dto) {
    super.beforeSave(entity, dto);
    UserDTO currentUser = getCurrentUser();
    if (!currentUser.getRole().equals(RoleEnum.ADMIN)
    && currentUser.getPasswordChange() == null) {
      throw new BaseException(401, "Bạn không đủ quyền truy cập");
    }

    if(dto.getUsername() == null){
      throw new BaseException(400, "username chưa nhập");
    }
    if(dto.getPassword() == null){
      dto.setPassword("1");
    }
    if (repository.existsByUsername(dto.getUsername(), entity.getId())) {
      throw new BaseException(400, "username đã tồn tại");
    }
    if(entity.getId() == null){
      entity.setPassword(DigestUtil.sha256Hex(dto.getPassword()));
      entity.setRole(RoleEnum.STUDENT);
      entity.setStatus((short) 0);
    }
    else{
      if (dto.getPasswordChange() != null) {
        entity.setPassword(DigestUtil.sha256Hex(dto.getPassword()));
      }
    }
  }


  @Override
  protected void specificMapToDTO(UserEntity entity, UserDTO dto) {
    super.specificMapToDTO(entity, dto);
    UserEntity createUser = getRepository().findById(entity.getCreatedBy()).get();
    UserEntity updateUser = getRepository().findById(entity.getUpdatedBy()).get();

    dto.setUserCreated(createUser.getFullName());
    dto.setUserUpdated(updateUser.getFullName());
  }


  @Override
  public ResponseEntity changePassword(UserDTO userDTO) {
    UserDTO user = getCurrentUser();
    if (!DigestUtil.sha256Hex(userDTO.getPassword()).equals(user.getPassword())) {
      throw new BaseException(400, "password not correct");
    }

    user.setPassword(userDTO.getPasswordChange());
    user.setPasswordChange("=))");

    save(getCurrentUserId(), user);

    return new ResponseEntity(user);
  }

  @Override
  public ResponseEntity resetPassword(UserDTO user) {
    user.setPassword("1");
    user.setPasswordChange("=))");

    save(user.getId(), user);

    return new ResponseEntity(user);
  }


  @Override
  public ResponseEntity register(UserDTO userDTO) {
    userDTO.setRole(RoleEnum.STUDENT);
    userDTO.setStatus((short) 0);
    save(userDTO);

    return new ResponseEntity(userDTO);
  }

  @Override
  public UserDTO getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }
    UserDetail userDetail = (UserDetail) authentication.getPrincipal();
    return mapToDTO(repository.findById(userDetail.getId()).get());
  }

  @Value("${application.security.config.tokenPrefix}")
  private String preFix;

  @Autowired
  private SecretKey secretKey;

  @Override
  public Long getCurrentUserId() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes()).getRequest();
    String token = request.getHeader("Authorization").replace(preFix, "");

    Long userId = Long.valueOf(Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token).getBody().get("userId").toString());
    return userId;
  }

  @Override
  @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
  public Page<UserDTO> search(UserDTO dto, Pageable pageable) {
    if (dto.getUsername() != null) {
      dto.setUsername('%' + dto.getUsername().replace(' ', '%') + '%');
    }
    if (dto.getFullName() != null) {
      dto.setFullName('%' + dto.getFullName().replace(' ', '%') + '%');
    }
    if (dto.getAddress() != null) {
      dto.setAddress('%' + dto.getAddress().replace(' ', '%') + '%');
    }
    if (dto.getPhoneNumber() != null) {
      dto.setPhoneNumber('%' + dto.getPhoneNumber().replace(' ', '%') + '%');
    }
    if (dto.getEmail() != null) {
      dto.setEmail('%' + dto.getEmail().replace(' ', '%') + '%');
    }

    return super.search(dto, pageable);
  }

  private final String[] ALLOW_EXTENSIONS = new String[]{"jpg", "png"};

  @Override
  public String uploadAvatar(MultipartFile file) {
    storageService.save(file);
    UserEntity u = getRepository().findById(getCurrentUserId()).get();
    u.setAvatar(file.getOriginalFilename());
    getRepository().save(u);
    return file.getOriginalFilename();
  }

  @Override
  @PreAuthorize("hasAnyRole('ADMIN')")
  public String uploadAvatar(MultipartFile file,Long id) {
    storageService.save(file);
    UserEntity u = getRepository().findById(id).get();
    u.setAvatar(file.getOriginalFilename());
    getRepository().save(u);
    return file.getOriginalFilename();
  }

  @Override
  @PreAuthorize("permitAll()")
  public ResponseEntity changeInfo(UserDTO userDTO) {
    UserEntity entity = getRepository().findById(getCurrentUserId()).get();
    UserEntity entityChange = mapToEntity(userDTO);
    entityChange.setId(entity.getIdChange());
    entityChange.setUsername(null);
    entityChange.setRole(entity.getRole());
    entityChange.setIdChange((long) -1);

    getRepository().save(entityChange);

    entity.setIdChange(entityChange.getId());
    getRepository().save(entity);

    return new ResponseEntity(200, Message.getMessage("Wait.Admin.Approve.Info"), userDTO);
  }

  @Override
  public ResponseEntity adminApproveChange(Long id) {
    UserEntity entity = getRepository().findById(id).get();
    UserEntity entityChange = getRepository().findById(entity.getIdChange()).get();

    Long idEntity = entity.getId();
    String userName = entity.getUsername();
    String password = entity.getPassword();
    RoleEnum role = entity.getRole();

    UserDTO dto = mapToDTO(entityChange);
    UserEntity entitySave = mapToEntity(dto);

    entitySave.setId(idEntity);
    entitySave.setUsername(userName);
    entitySave.setPassword(password);
    entitySave.setRole(role);
    entitySave.setIdChange(null);

    getRepository().deleteById(entityChange.getId());
    getRepository().save(entitySave);

    return new ResponseEntity(200, Message.getMessage("Admin.approve.Change.Info"));
  }

  @Override
  public UserDTO approveTeacher(Long id, UserDTO dto) {
    if (dto.getSubjectsId() == null || dto.getSubjectsId().size() == 0) {
      throw new BaseException(400, Message.getMessage("subjectIds.null.or.empty"));
    }
    UserEntity userDTO = getRepository().findById(id).get();
    userDTO.setRole(RoleEnum.TEACHER);
    List<SubjectEntity> subjects = new ArrayList<>();
    for (Long subjectId : dto.getSubjectsId()) {
      SubjectEntity subjectDTO = subjectRepository.findById(subjectId).get();
      subjects.add(subjectDTO);
    }
    userDTO.setSubjects(subjects);
    userDTO = getRepository().save(userDTO);

    return mapToDTO(userDTO);
  }

  @Override
  public UserDTO profile() {
    return getCurrentUser();
  }

  @Override
  public String importData(MultipartFile file, int sheetNo, int startLineNo) {

    storageService.save(file);

    Path path = Paths.get("/uploads/" + file.getOriginalFilename());

    List<List<String>> data = ExcelUtil.readFile(path, sheetNo, startLineNo);

    int objCount = data.size();

    for (int i = 1; i < objCount; i++) {
      UserDTO userDTO = new UserDTO();
      List<String> useConvert = data.get(i);
      String name = useConvert.get(0);
      String username = StringUtils.stripAccents(name.replace(" ", "").toLowerCase());

      int j = 1;
      while (repository.findByUsername(username).isPresent()) {
        username = username + j;
        j++;
      }
      String age = useConvert.get(1);
      if (age != null) {
        userDTO.setAge(Long.parseLong(age));
      }

      userDTO.setCmnd(useConvert.get(2));

      if (useConvert.get(3) != null) {
        if (useConvert.get(3).equals(0)) {
          userDTO.setGender(GenderEnum.NAM);
        } else {
          userDTO.setGender(GenderEnum.NU);
        }
      }
      userDTO.setAddress(useConvert.get(4));
      userDTO.setEmail(useConvert.get(5));
      userDTO.setPhoneNumber(useConvert.get(6));
      userDTO.setFullName(name);
      userDTO.setUsername(username);
      save(userDTO);
    }
    return "Import successful";
  }

}
