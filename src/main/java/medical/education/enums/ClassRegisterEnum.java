package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum ClassRegisterEnum implements IEnum {

  THANH_CONG((short) 0, "Thành Công"),
  LOP_BI_HUY((short) 1, "Lớp Bị Hủy");

  private short value;
  private String name;

  ClassRegisterEnum(short value, String name) {
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

  public short getValue() {
    return value;
  }

  public void setValue(short value) {
    this.value = value;
  }
}
