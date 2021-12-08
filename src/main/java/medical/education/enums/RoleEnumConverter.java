package medical.education.enums;

import javax.persistence.Converter;
import spring.backend.library.enums.EnumConverter;

@Converter(autoApply = true)
public class RoleEnumConverter extends EnumConverter<RoleEnum> {

}
