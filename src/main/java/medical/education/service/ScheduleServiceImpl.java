package medical.education.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import medical.education.dao.model.CourseEntity;
import medical.education.dao.model.RegisterEntity;
import medical.education.dao.model.ScheduleEntity;
import medical.education.dao.repository.CourseRepository;
import medical.education.dao.repository.PlaceRepository;
import medical.education.dao.repository.ScheduleRepository;
import medical.education.dao.repository.SubjectRepository;
import medical.education.dao.repository.UserRepository;
import medical.education.dto.ScheduleDTO;
import medical.education.dto.UserDTO;
import medical.education.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.msg.Message;
import spring.backend.library.service.AbstractBaseService;
import spring.backend.library.util.DateUtil;
import spring.backend.library.utils.DateUtils;

@Service
public class ScheduleServiceImpl extends
        AbstractBaseService<ScheduleEntity, ScheduleDTO, ScheduleRepository>
        implements ScheduleService {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Override
    protected ScheduleRepository getRepository() {
        return scheduleRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    protected void beforeSave(ScheduleEntity entity, ScheduleDTO dto) {
        super.beforeSave(entity, dto);
        if (dto.getDay() == null) {
            throw new BaseException(400, Message.getMessage("data.null", new Object[]{"Thứ"}));
        }

        if (dto.getPlaceId() == null || !placeRepository.existsById(dto.getPlaceId())) {
            throw new BaseException(400, Message.getMessage("data.null", new Object[]{"Địa điểm"}));
        }

        if (dto.getSubjectId() == null || !subjectRepository.existsById(dto.getSubjectId())) {
            throw new BaseException(400, Message.getMessage("data.null", new Object[]{"Môn học"}));
        }

        if (dto.getCourseId() == null || !courseRepository.existsById(dto.getCourseId())) {
            throw new BaseException(400, Message.getMessage("data.null", new Object[]{"Khóa học"}));
        }

        if (dto.getTeacherId() == null || !userRepository.existsById(dto.getTeacherId())) {
            throw new BaseException(400,
                    Message.getMessage("data.null", new Object[]{"Giảng viên"}));
        }
        if (dto.getKipHoc() == null) {
            throw new BaseException(400, Message.getMessage("data.null", new Object[]{"Kíp học"}));
        }
        if (dto.getReason() == null)
            kiemTraLopHoc(dto.getTeacherId(), dto.getDates(), dto.getKipHoc());
    }

    private void kiemTraLopHoc(Long id, String dtoDate, Short kipHoc) {
        List<ScheduleEntity> scheduleEntityList = getRepository().findByTeacherId(id).orElse(null);
        if (scheduleEntityList == null) {
            return;
        } else {

            String[] dateCheck = dtoDate.replace("[", "").replace("]", "").replace("\"", "")
                    .split(",");

            for (ScheduleEntity s : scheduleEntityList) {
                String date = s.getDates().replace("[", "").replace("]", "").replace("\"", "");
                String[] datess = date.split(",");
                int kip = s.getKipHoc();
                for (int i = 0; i < dateCheck.length; i++) {
                    for (int j = 0; j < datess.length; j++) {
                        if (dateCheck[i].equals(datess[j]) && kip == kipHoc) {
                            throw new BaseException("Trùng lịch");
                        }
                    }
                }
            }
        }

        return;
    }

    @Override
    protected void afterSave(ScheduleEntity entity, ScheduleDTO dto) {
        super.afterSave(entity, dto);
        entity.setSubject(subjectRepository.findById(entity.getSubjectId()).get());
        entity.setPlace(placeRepository.findById(entity.getPlaceId()).get());
        entity.setTeacher(userRepository.findById(entity.getTeacherId()).get());
    }

    @Override
    protected void specificMapToDTO(ScheduleEntity entity, ScheduleDTO dto) {
        super.specificMapToDTO(entity, dto);

        if (entity.getChangeInformation() != null) {
            dto.setChangeInfo(
                    scheduleService.findDetailById(entity.getChangeInformation().getId()));
        }
//    dto.setCourseInfo(courseService.findById(entity.getCourseId()));
        if (entity.getCourseId() != null) {
            CourseEntity course = courseRepository.findById(entity.getCourseId()).orElse(null);
            if (course != null) {
                dto.setNameCourse(course.getProgramEntity().getName());
                dto.setCodeCourse(course.getProgramEntity().getCode());
                if (course.getRegisterEntities() != null) {
                    int count = 0;
                    for (RegisterEntity e : course.getRegisterEntities()
                    ) {
//                        if()
                    }
                    dto.setNumberRegister(course.getRegisterEntities().size());
                }
                dto.setStatus((int) course.getStatus());
            }
        }
        dto.setSubjectInfo(subjectService.findById(entity.getSubjectId()));
        dto.setPlaceInfo(placeService.findById(entity.getPlaceId()));
        dto.setTeacher(userService.findById(entity.getTeacherId()));
    }

    @Override
    // sinh viên xem lịch
    public Page<ScheduleDTO> getSchedule() {
        ScheduleDTO scheduleSearch = new ScheduleDTO();
        scheduleSearch.setCourseId(userService.getCurrentUser().getCurrentCourseId());
        return search(scheduleSearch, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Override
    public Page<ScheduleDTO> getListClass(ScheduleDTO dto, Pageable page) {
        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(RoleEnum.TEACHER)) {
            throw new BaseException(400, Message.getMessage("not.teacher"));
        }

        dto.setTeacherId(currentUser.getId());
        return super.search(dto, page);
    }

//  @Override
//  public Page<ScheduleDTO> getSchedulebBusy() {
//    ScheduleDTO scheduleSearch = new ScheduleDTO();
//    scheduleSearch.setStatus(2);
//    return search(scheduleSearch, PageRequest.of(0, Integer.MAX_VALUE));
//  }

    @Override
    public ScheduleDTO handleChangeSchedule(Long id, ScheduleDTO dto) {
        save(id, dto);
        delete(dto.getDeleteId());
        return dto;
    }

    @Override
    public Integer countChange() {
        return getRepository().countChange();
    }

    @Override
    public Page<ScheduleDTO> findAllChange(Pageable page) {
        return getRepository().findAllChange(page).map(this::mapToDTO);
    }

    @Override
    public ScheduleDTO changeSchedule(Long id, ScheduleDTO dto) {
        ScheduleEntity entity = getRepository().findById(id).get();
        dto.setId(null);
        dto.setStatus(1);
        dto.setChangeScheduleId(null);
        ScheduleDTO changeDTO = save(dto);
        entity.setChangeScheduleId(changeDTO.getId());
        getRepository().save(entity);
        return mapToDTO(entity);
    }
}
