package medical.education.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import spring.backend.library.enums.EnumConverter;

@Converter(autoApply = true)
public class ClassStatusEnumConverter extends EnumConverter<ClassStatusEnum> {

}
