package medical.education.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import medical.education.dao.model.CourseEntity;
import medical.education.dao.model.RegisterEntity;
import medical.education.dao.model.ResultEntity;
import medical.education.dao.model.SubjectEntity;
import medical.education.dao.model.UserEntity;
import medical.education.dao.repository.CourseRepository;
import medical.education.dao.repository.ProgramRepository;
import medical.education.dao.repository.RegisterRepository;
import medical.education.dao.repository.ResultRepository;
import medical.education.dao.repository.UserRepository;
import medical.education.dto.RegisterDTO;
import medical.education.dto.ResultDTO;
import medical.education.dto.UserDTO;
import medical.education.enums.CourseStatusEnum;
import medical.education.enums.RegisterEnum;
import medical.education.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.msg.Message;
import spring.backend.library.service.AbstractBaseService;

@Service
//@PreAuthorize("hasAnyRole('TEARCHER', 'ADMIN', 'STUDENT')")
public class RegisterServiceImpl extends
        AbstractBaseService<RegisterEntity, RegisterDTO, RegisterRepository>
        implements RegisterService {

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramService programService;

    @Override
    protected RegisterRepository getRepository() {
        return registerRepository;
    }

    @Override
    protected void beforeSave(RegisterEntity entity, RegisterDTO dto) {
        super.beforeSave(entity, dto);
        UserDTO currentUser = userService.getCurrentUser();
        if (dto.getCourseId() == null || !courseRepository.existsById(dto.getCourseId())) {
            throw new BaseException(400,
                    Message.getMessage("Null.Or.Not.Exist", new Object[]{"code"}));
        }

        if (currentUser.getRole().equals(RoleEnum.ADMIN)) {
            // admin phê duyệt đỗ tốt nghiệp cho sinh viên nêu là admin
            if (dto.getSemester() == null) {
                throw new BaseException(410, "semester is null");
            }
            if (dto.getCourseId() == null) {
                throw new BaseException(410, "courseId is null");
            }
            if (dto.getStudentId() == null) {
                throw new BaseException(410, "studentId is null");
            }
            if (dto.getStatus() == null) {
                throw new BaseException(410, "status is null");
            }
        } else {
            // sinh viên đăng ký khóa nếu role là sinh viên
            entity.setStudentId(userService.getCurrentUserId());

            RegisterEntity e = registerRepository.findByCourseIdAndStudentId(
                    currentUser.getCurrentCourseId(), userService.getCurrentUserId());
            if (currentUser.getCurrentCourseId() != null) {
                throw new BaseException(410,
                        Message.getMessage("Has.Register.Course",
                                new Object[]{e.getCourse().getProgramEntity().getName()}));
            } else {
                CourseEntity entityCourse = courseRepository.findById(dto.getCourseId()).get();
                if (entityCourse.getRegisterEntities() != null) {
                    if (entityCourse.getRegisterEntities().size() >= entityCourse
                            .getLimitRegister()) {
                        throw new BaseException(430, "Khóa học đã quá giới hạn đăng ký");
                    }
                }
                if (entity.getStatus() == null && !entityCourse.getStatus()
                        .equals(CourseStatusEnum.THOI_GIAN_DANG_KI.getValue())) {
                    throw new BaseException(431, "Chỉ có thể đăng ký khóa trong thời gian đăng ký");
                }
                entity.setSemester(entityCourse.getSemester());

                courseRepository.save(entityCourse);

                UserEntity userEntity = userRepository.findById(currentUser.getId()).get();
                userEntity.setCurrentCourseId(dto.getCourseId());
                userRepository.save(userEntity);
            }
            entity.setStatus(RegisterEnum.REGISTER_DONED);
        }
    }

    @Override
    public void changeSemester(Long courseId, Integer semester) {
        List<RegisterEntity> listChange = registerRepository
                .findRegistersToChangeSemester(courseId);
        for (RegisterEntity e : listChange) {
            e.setSemester(semester);
        }
        registerRepository.saveAll(listChange);
    }

    @Override
    protected void afterSave(RegisterEntity entity, RegisterDTO dto) {
        super.afterSave(entity, dto);
        resultService
                .generateResultsForStudent(entity.getCourseId(), entity.getStudentId(),
                        entity.getId());
        entity.setCourse(courseRepository.findById(entity.getCourseId()).get());
        entity.setStudent(userRepository.findById(entity.getStudentId()).get());
    }

    @Override
    protected void specificMapToDTO(RegisterEntity entity, RegisterDTO dto) {
        super.specificMapToDTO(entity, dto);
        dto.setStudentInfo(userService.findDetailById(entity.getStudentId()));
        dto.setCourseInfo(courseService.findDetailById(entity.getCourseId()));
        dto.setProgramInfo(programService.findDetailById(entity.getCourse().getProgramId()));
    }

    @Override
    public List<Integer> getListSemester(Long courseId) {
        return getRepository().getListSemester(courseId);
    }

    // để hủy khóa học. dành cho sinh viên
    @Override
    public void delete(Long id) {
        RegisterEntity entity = getRepository().findById(id).get();
        if (!entity.getStatus().equals(RegisterEnum.REGISTER_DONED)) {
            throw new BaseException(411, Message.getMessage("Cant.cancel.course"));
        }
        UserDTO currentUser = userService.getCurrentUser();

        ResultDTO searchDTO = new ResultDTO();
        searchDTO.setStudentId(currentUser.getId());
        searchDTO.setCourseId(entity.getCourseId());
        List<ResultEntity> listResult = resultRepository
                .search(searchDTO, PageRequest.of(0, Integer.MAX_VALUE)).toList();

        resultRepository.deleteAll(listResult);

        UserEntity user = userRepository.findById(currentUser.getId()).get();
        user.setCurrentCourseId(null);
        userRepository.save(user);

        super.delete(id);
    }

    @Override
    public void synchronizedData() {
        scheduleEveryDay();
    }

    // Cập nhật trạng thái và tính điểm trung bình
    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleEveryDay() {
        List<RegisterEntity> allRegister = StreamSupport
                .stream(getRepository().findAll().spliterator(), false)
                .collect(Collectors.toList());
        List<RegisterEntity> listSave = new ArrayList<>();
        for (RegisterEntity e : allRegister) {
            if (e.getCourse().getStatus().equals(CourseStatusEnum.DANG_HOC.getValue()) &&
                    e.getStatus().equals(RegisterEnum.REGISTER_DONED)) {
                e.setStatus(RegisterEnum.STUDYING);
                listSave.add(e);
            } else if (e.getCourse().getStatus().equals(CourseStatusEnum.HOAN_THANH.getValue()) &&
                    e.getStatus().equals(RegisterEnum.STUDYING)) {
                e.setStatus(RegisterEnum.WAIT_TEACHER);

                boolean daNhapHetDiem = true;

                for (ResultEntity resultEntity : e.getResults()) {

                }
                if (daNhapHetDiem) {
                    Double total = 0.0;
                    int count = 0;
                    for (ResultEntity r : e.getResults()) {

                        total += r.getTotal();
                        count++;

                    }
                    if (count > 0) {
                        total = total / count;
                        e.setTotal(total);
                        e.setKind(applyKind(total));
                    }
                    if (total >= 4) {
                        e.setStatus(RegisterEnum.DONED);
                    } else {
                        e.setStatus(RegisterEnum.FAIL);
                    }
                }
                listSave.add(e);
            }

            // tính điểm trung bình

        }
        registerRepository.saveAll(listSave);
    }

    public String applyKind(Double total) {
        if (total > 8) {
            return "Giỏi";
        }
        if (total > 6.25) {
            return "Khá";
        }
        if (total > 5) {
            return "Trung Bình";
        }
        return "Kém";
    }

    // Tạo bảng điểm để count ở màn danh sách sinh viên quyền giảng viên đăng ký khóa học trạng thái 2 liền tạo ra bảng điểm
    @Scheduled(cron = "0 0 1 * * *")
    public void createResult() {
        Iterable<RegisterEntity> registerEntities = getRepository().findAll();
        for (RegisterEntity e : registerEntities) {
            if (e.getCourse() != null) {
                if (e.getCourse().getStatus() == 2) {
                    if (e.getCourse() != null) {
                        List<SubjectEntity> subjectEntities = e.getCourse().getProgramEntity()
                                .getSubjects();
                        for (SubjectEntity subject : subjectEntities) {
                            if (!resultRepository
                                    .resultExistByCourseIdAndStudentIdAndSubjectId(e.getCourseId(),
                                            e.getStudentId(), subject.getId())) {
                                ResultEntity resultEntity = new ResultEntity();
                                resultEntity.setRegisterId(e.getId());
                                resultEntity.setCourseId(e.getCourseId());
                                resultEntity.setStudentId(e.getStudentId());
                                resultEntity.setSubjectId(subject.getId());
                                resultRepository.save(resultEntity);
                            }
                        }
                    }
                }
            }
        }
    }
}
