package medical.education.enums;

import javax.persistence.Converter;
import spring.backend.library.enums.EnumConverter;

@Converter(autoApply = true)
public class StudyProcessEnumConverter extends EnumConverter<StudyProcessEnum> {

}
