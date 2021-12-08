package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum RoleEnum implements IEnum {
  ADMIN((short) 1, "ADMIN"),
  TEACHER((short) 2, "TEACHER"),
  STUDENT((short) 3, "STUDENT");

  public short value;
  public String name;

  RoleEnum(short value, String name) {
    this.value = value;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public short getValue() {
    return value;
  }

  public void setValue(short value) {
    this.value = value;
  }
}
