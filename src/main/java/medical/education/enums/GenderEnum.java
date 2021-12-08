package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum GenderEnum implements IEnum {

  NAM((short) 0, "Nam"),
  NU((short) 1, "Nữ");

  private short value;
  private String name;

  @Override
  public String getName() {
    return name;
  }

  GenderEnum(short value, String name) {
    this.value = value;
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public short getValue() {
    return value;
  }

  public void setValue(short value) {
    this.value = value;
  }

}
