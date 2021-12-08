package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum NotificationEnum implements IEnum {

  CHUA_DOC((short) 1, "Chưa đọc"),
  DA_DOC((short) 2, "Đã đọc");

  private short value;
  private String name;

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  NotificationEnum(short value, String name) {
    this.value = value;
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
