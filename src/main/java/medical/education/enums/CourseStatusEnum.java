package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum CourseStatusEnum implements IEnum {

  THOI_GIAN_DANG_KI((short) 1, "Thời gian đăng kí"),
  DANG_HOC((short) 2, "Đang học"),
  HOAN_THANH((short) 3, "Hoàn Thành"),
  HUY_KHOA((short) 4, "Hủy");

  private short value;
  private String name;

  CourseStatusEnum(short value, String name) {
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
