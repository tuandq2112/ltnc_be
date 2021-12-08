package medical.education.service;

import java.util.ArrayList;
import java.util.List;
import medical.education.dao.model.CourseEntity;
import medical.education.dao.model.ResultEntity;
import medical.education.dao.model.SubjectEntity;
import medical.education.dao.repository.CourseRepository;
import medical.education.dao.repository.RegisterRepository;
import medical.education.dao.repository.ResultRepository;
import medical.education.dao.repository.SubjectRepository;
import medical.education.dao.repository.UserRepository;
import medical.education.dto.ResultDTO;
import medical.education.dto.UserDTO;
import medical.education.enums.CourseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import spring.backend.library.exception.BaseException;
import spring.backend.library.msg.Message;
import spring.backend.library.service.AbstractBaseService;

@Service
@PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
public class ResultServiceImpl extends
        AbstractBaseService<ResultEntity, ResultDTO, ResultRepository>
        implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private RegisterRepository registerRepository;

    @Override
    protected ResultRepository getRepository() {
        return resultRepository;
    }

    @Override
    @PreAuthorize("hasAnyRole('TEARCHER', 'ADMIN')")
    protected void beforeSave(ResultEntity entity, ResultDTO dto) {
        super.beforeSave(entity, dto);
        if (entity.getCourse() != null) {
            CourseEntity courseEntity = entity.getCourse();

            if ((courseEntity.getStatus() == 1 || courseEntity.getStatus() == 3) && (
                    dto.getMidPoint() != null || dto.getEndPoint() != null)) {
                throw new BaseException(400, "Khóa học chưa bắt đầu hoặc đã kết thúc");
            }
        }
        if (dto.getStudentId() == null || !userRepository.existsById(dto.getStudentId())) {
            throw new BaseException(400, "studentId is null or not exist");
        }
        if (dto.getCourseId() == null || !courseRepository.existsById(dto.getCourseId())) {
            throw new BaseException(400, "courseId is null or not exist");
        }
        if (dto.getSubjectId() == null || !subjectRepository.existsById(dto.getSubjectId())) {
            throw new BaseException(400, "subjectId is null or not exist");
        }
        if (dto.getRegisterId() == null || !registerRepository.existsById(dto.getRegisterId())) {
            throw new BaseException(400, "registerId is null or not exist");
        }
        if (dto.getEndPoint() != null && dto.getMidPoint() < 0 && dto.getMidPoint() > 10) {
            throw new BaseException(400, "0 <= Midpoint <= 10");
        }
        if (dto.getEndPoint() != null && dto.getEndPoint() < 0 && dto.getEndPoint() > 10) {
            throw new BaseException(400, "0 <= Endpoint <= 10");
        }
        if (entity.getSubject() != null) {
            SubjectEntity subjectEntity = entity.getSubject();
            if (dto.getEndPoint() != null && dto.getMidPoint() != null
                    && subjectEntity.getMidtermScore() != null
                    && subjectEntity.getFinalScore() != null) {
                entity.setTotal(dto.getMidPoint() * subjectEntity.getMidtermScore()
                        + dto.getEndPoint() * subjectEntity.getFinalScore());
            }
        }

        if (entity.getMuster() == null) {
            entity.setMuster(0);
        }
        if (entity.getId() != null && !entity.getCourse().getStatus()
                .equals(CourseStatusEnum.DANG_HOC.getValue())) {
            throw new BaseException(432, "Chỉ có thể nhập điểm trong thời gian học");
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN','STUDENT')")
    /** tạo bảng kết quả trống khi sinh viên đăng ký để trong quá trình học
     * giảng viên nhập điểm vào kết quả
     */
    public void generateResultsForStudent(Long courseId, Long studentId, Long registerId) {
        CourseEntity courseEntity = courseRepository.findById(courseId).get();
        List<ResultEntity> listResult = new ArrayList<>();

        for (SubjectEntity subjectEntity : courseEntity.getProgramEntity().getSubjects()) {
            ResultEntity result = new ResultEntity();
            result.setRegisterId(registerId);
            result.setStudentId(studentId);
            result.setCourseId(courseId);
            result.setMuster(0);
            result.setSubjectId(subjectEntity.getId());
            listResult.add(result);
        }
        getRepository().saveAll(listResult);
    }

    @Override
    protected void specificMapToDTO(ResultEntity entity, ResultDTO dto) {
        super.specificMapToDTO(entity, dto);
        dto.setSubjectInfo(subjectService.findById(entity.getSubjectId()));
        dto.setStudentInfo(userService.findById(entity.getStudentId()));
        if (dto.getTotal() != null) {
            dto.setRank(applyRank(dto.getTotal()));
        }
    }

    @Override
    public ResultDTO attendance(Long id) {
        if (!getRepository().existsById(id)) {
            throw new BaseException(421, Message.getMessage("no.id", new Object[]{id}));
        }
//    ResultDTO result = findById(id);
        ResultEntity entity = getRepository().findById(id).get();
        if (entity.getSubject() != null &&
                entity.getMuster() < entity.getSubject().getLesson()) {
            entity.setMuster(entity.getMuster() + 1);
        } else {
            throw new BaseException(420, Message.getMessage("attendance.out"));
        }
        if (!entity.getCourse().getStatus()
                .equals(CourseStatusEnum.DANG_HOC.getValue())) {
            throw new BaseException(432, "Chỉ có thể điểm danh trong thời gian học");
        }

        return mapToDTO(getRepository().save(entity));
    }

    @Override
    @PreAuthorize("hasAnyRole('STUDENT')")
    public Page<ResultDTO> getResult(ResultDTO searchDTO, Pageable page) {
        UserDTO currentUser = userService.getCurrentUser();
        searchDTO.setStudentId(currentUser.getId());
        if (searchDTO.getCourseId() == null) {
            searchDTO.setCourseId(currentUser.getCurrentCourseId());
        }
        return search(searchDTO, page);
    }

    public String applyRank(Double total) {
        if (total > 9.0) {
            return "A+";
        }
        if (total > 8.5) {
            return "A";
        }
        if (total > 8.0) {
            return "B+";
        }
        if (total > 7.0) {
            return "B";
        }
        if (total > 6.5) {
            return "C+";
        }
        if (total > 6.0) {
            return "C";
        }
        if (total > 5.0) {
            return "D+";
        }
        if (total > 4.0) {
            return "D";
        }
        if (total > 3.0) {
            return "E";
        }
        return "F";
    }

}
