package medical.education.service;

import com.google.common.base.Strings;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import medical.education.dao.model.CourseEntity;
import medical.education.dao.model.NotificationEntity;
import medical.education.dao.model.RegisterEntity;
import medical.education.dao.model.ScheduleEntity;
import medical.education.dao.model.SubjectEntity;
import medical.education.dao.model.UserEntity;
import medical.education.dao.repository.CourseRepository;
import medical.education.dao.repository.HealthFacilityRepository;
import medical.education.dao.repository.NotificationRepository;
import medical.education.dao.repository.RegisterRepository;
import medical.education.dao.repository.ScheduleRepository;
import medical.education.dao.repository.SubjectRepository;
import medical.education.dto.CourseDTO;
import medical.education.dto.ScheduleDTO;
import medical.education.dto.SubjectDTO;
import medical.education.dto.UserDTO;
import medical.education.enums.CourseStatusEnum;
import medical.education.enums.RegisterEnum;
import medical.education.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.service.AbstractBaseService;

@Service
public class CourseServiceImpl extends
        AbstractBaseService<CourseEntity, CourseDTO, CourseRepository> implements CourseService {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private HealthFacilityRepository healthFacilityRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProgramService programService;

    @Autowired
    private RegisterService registerService;

    @Override
    protected CourseRepository getRepository() {
        return repository;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN')")
    protected void beforeSave(CourseEntity entity, CourseDTO dto) {
        super.beforeSave(entity, dto);

        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getRole().equals(RoleEnum.ADMIN)) {
            throw new BaseException(401, "B???n kh??ng ????? quy???n truy c???p");
        }
        if (Strings.isNullOrEmpty(entity.getCode())) {
            Long i = entity.getId() == null ? getRepository().count() : entity.getId();
            String newCode = "COURSE_" + String.format("%04d", i);
            dto.setCode(newCode);
            entity.setCode(newCode);
        }
        if (getRepository().existsByProgramIdAndHealthFacilityId(dto.getProgramId(),
                dto.getHealthFacilityId())) {
            if (!getRepository().choPhepMoKhoaMoi(dto.getProgramId(), dto.getHealthFacilityId())
                    && dto.getId() == null) {
                throw new BaseException("Kh??a ??ang trong tr???ng th??i ????ng k?? k th??? m???");
            }
        }

//    if (dto.getSemester() == null) {
//      throw new BaseException(451, "Ch??a nh???p k??? h???c");
//    }
//    if (dto.getPrice() == null) {
//      throw new BaseException(400, "Ch??a nh???p gi??");
//    }
//    if (dto.getNumberLesson() == null) {
//      throw new BaseException(400, "Ch??a nh???p S??? ti???t h???c");
//    }
        if (dto.getLimitRegister() == null) {
            throw new BaseException(400, "Ch??a nh???p gi???i h???n ????ng k??");
        }
        if (dto.getHealthFacilityId() == null || !healthFacilityRepository
                .existsById(dto.getHealthFacilityId())) {
            throw new BaseException(400, "Ch??a nh???p ho???c kh??ng t???n t???i C?? s??? ????o t???o");
        }

        if (entity.getStatus() == null) {
            entity.setStatus(CourseStatusEnum.THOI_GIAN_DANG_KI.getValue());
        }
        if (dto.getNgayKhaiGiang() == null) {
            throw new BaseException(410, "B???n ch??a nh???p ng??y khai gi???ng");
        }
        if (dto.getNgayKetThuc() == null) {
            throw new BaseException(410, "B???n ch??a nh???p ng??y k???t th??c h???c k???");
        }
        if (dto.getNgayKhaiGiang().after(dto.getNgayKetThuc())) {
            throw new BaseException(451, "Ng??y khai gi???ng ph???i tr?????c ng??y k???t th??c kh??a");
        }
        if (entity.getStatus().equals(CourseStatusEnum.HOAN_THANH.getValue())
                && entity.getNgayKhaiGiang().after(new Date())) {
            entity.setStatus(CourseStatusEnum.THOI_GIAN_DANG_KI.getValue());
            scheduleRepository.deleteAll(entity.getSchedules());
        }
//    if (dto.getNgayKhaiGiang() != null && entity.getStatus()
//        .equals(CourseStatusEnum.DANG_HOC.getValue())) {
//      throw new BaseException(403, "Kh??ng th??? thay ?????i th??ng tin kh??a h???c trong th???i gian h???c");
//    }
//    if (entity.getId() != null) {
//      LocalDate ngayKhaiGiang = entity.getNgayKhaiGiang().toInstant()
//          .atZone(ZoneId.systemDefault()).toLocalDate();
//      Integer semester = ngayKhaiGiang.getYear()*100
//          + ngayKhaiGiang.getMonthValue();
//      entity.setSemester(semester);
//      registerService.changeSemester(entity.getId(), semester);
//    }

    }

    @Override
    public void delete(Long id) {
        if (!getRepository().existsById(id)) {
            throw new BaseException(480, "Kh??a h???c kh??ng t???n t???i");
        }
        CourseEntity entity = getRepository().findById(id).get();
        if (entity.getStatus().equals(CourseStatusEnum.DANG_HOC.getValue())) {
            throw new BaseException(481, "Kh??ng th??? x??a kh??a h???c trong th???i gian h???c");
        } else {
            List<UserEntity> listRegister = new ArrayList<>();
            if (entity.getRegisterEntities() != null) {
                for (RegisterEntity e : entity.getRegisterEntities()
                ) {
                    if (e.getStudent() != null) {
                        listRegister.add(e.getStudent());
                    }
                }
            }
            List<NotificationEntity> notifications = new ArrayList<>();
            List<RegisterEntity> registers = new ArrayList<>();

            for (UserEntity e : listRegister) {
                NotificationEntity notification = new NotificationEntity();
                notification.setContent(
                        "Kh??a h???c: " + entity.getName() + ", K??? h???c: " + entity.getSemester()
                                + " ???? b??? h???y. ");
                notification.setOwnerId(e.getId());
                notifications.add(notification);

                RegisterEntity register = registerRepository
                        .findByCourseIdAndStudentId(entity.getId(), e.getId());
                if (register != null) {
                    register.setStatus(RegisterEnum.CANCEL);
                    registers.add(register);
                }
            }

            registerRepository.saveAll(registers);
            notificationRepository.saveAll(notifications);
        }
        super.delete(id);
    }

    @Override
    protected void specificMapToDTO(CourseEntity entity, CourseDTO dto) {
        super.specificMapToDTO(entity, dto);
        dto.setNumberRegister(registerRepository.countByCourseId(entity.getId()));
        if (entity.getProgramEntity() != null) {
            dto.setProgramInfo(programService.findById(entity.getProgramId()));
        }

        if (entity.getSchedules() != null) {
            List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
            for (ScheduleEntity e : entity.getSchedules()) {
                ScheduleDTO scheduleDTO = scheduleService.findById(e.getId());
                if (scheduleDTO.getChangeScheduleId() == null) {
                    scheduleDTOS.add(scheduleDTO);
                }
            }
            dto.setListSchedules(scheduleDTOS);
        }
        if (entity.getProgramEntity() != null) {
            if (entity.getProgramEntity().getSubjects() != null) {
                List<SubjectDTO> subjects = new ArrayList<>();
                Integer countLesson = 0;
                for (SubjectEntity e : entity.getProgramEntity().getSubjects()) {
                    if (e.getLesson() != null) {
                        countLesson += e.getLesson();
                    }
                    SubjectDTO sj = subjectService.findById(e.getId());
                    if (scheduleRepository
                            .existsByCourseIdAndSubjectId(entity.getId(), e.getId())) {
                        sj.setHasScheduled(true);
                    }
                    subjects.add(sj);
                }
                dto.getProgramInfo().setListSubjects(subjects);
                dto.setNumberLesson(countLesson);
            }
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public Page<CourseDTO> search(CourseDTO dto, Pageable pageable) {
        if (dto.getName() != null) {
            dto.setName("%" + dto.getName().trim()
                    .replaceAll(" ", "%") + "%");
        }
        if (dto.getCode() != null) {
            dto.setCode("%" + dto.getCode().trim()
                    .replaceAll(" ", "%") + "%");
        }
        if (dto.getNameHealthFacility() != null) {
            dto.setNameHealthFacility("%" + dto.getNameHealthFacility()
                    .trim().replaceAll(" ", "%") + "%");
        }
        if (dto.getNameUserCreated() != null) {
            dto.setNameUserCreated(
                    "%" + dto.getNameUserCreated().trim().replaceAll(" ", "%") + "%");
        }
        if (dto.getStatus() != null && dto.getStatus() == 1) {
            dto.setDate(new java.sql.Date(System.currentTimeMillis()).toString());
        }
        return super.search(dto, pageable);
    }

    @Override
    public void synchronizedData() {
        update();
        registerService.synchronizedData();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void update() {
        List<CourseEntity> allCourse = repository.search(new CourseDTO(),
                PageRequest.of(0, Integer.MAX_VALUE)).toList();
        List<CourseEntity> listSave = new ArrayList<>();
        List<CourseEntity> courseEntities = new ArrayList<>();
        for (CourseEntity e : allCourse) {
            List<ScheduleEntity> listSchedule = e.getSchedules();
            if (e.getStatus().equals(CourseStatusEnum.THOI_GIAN_DANG_KI.getValue()) &&
                    listSchedule.size() == 0) {
                courseEntities.add(e);
            }
            if (e.getNgayKhaiGiang() != null && e.getNgayKhaiGiang().before(new Date())
                    && e.getStatus().equals(CourseStatusEnum.THOI_GIAN_DANG_KI.getValue())) {
                if (e.getRegisterEntities() == null || e.getRegisterEntities().size() < e.getMinRegister()) {
                    e.setStatus(CourseStatusEnum.HUY_KHOA.getValue());
                    listSave.add(e);
                } else {
                    e.setStatus(CourseStatusEnum.DANG_HOC.getValue());
                    listSave.add(e);
                }
            } else if (e.getNgayKetThuc() != null && e.getNgayKetThuc().before(new Date())
                    && e.getStatus().equals(CourseStatusEnum.DANG_HOC.getValue())) {
                e.setStatus(CourseStatusEnum.HOAN_THANH.getValue());
                listSave.add(e);
            }
        }
        if (courseEntities.size() > 0) {
            NotificationEntity noti = new NotificationEntity();
            noti.setRole(RoleEnum.ADMIN.value);
            noti.setContent(createContentNotification(courseEntities));
            noti.setIsRead((short) 0);
            noti.setAuditProperties(ZonedDateTime.now(), 1L, ZonedDateTime.now(), 1L);
            notificationRepository.save(noti);
        }
        getRepository().saveAll(listSave);
    }

    public String createContentNotification(List<CourseEntity> listCourse) {
        StringBuilder str = new StringBuilder();
        str.append("H??? TH???NG XIN TH??NG B??O");
        int index = 1;
        for (CourseEntity course : listCourse) {
            str.append("\n")
                    .append(index++)
                    .append(". Ch????ng tr??nh ????o t???o: ")
                    .append(course.getProgramEntity().getName())
                    .append(", Kh??a h???c: ")
                    .append(course.getSemester())
                    .append(", Ng??y khai gi???ng: ")
                    .append(course.getNgayKhaiGiang());
        }
        str.append(
                "\n ch??a ???????c x???p l???ch gi???ng d???y. y??u c???u c??c qu???n tr??? x???p l???ch tr?????c ng??y khai gi???ng");

        return str.toString();

    }
}
