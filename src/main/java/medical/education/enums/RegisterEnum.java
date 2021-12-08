package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum RegisterEnum implements IEnum {

  REGISTER_DONED((short) 1, "Đăng ký thành công"),
  STUDYING((short) 2, "Đang học"),
  WAIT_TEACHER((short) 3, "Chờ giảng viên nhập điểm"),
  DONED((short) 4, "Hoàn thành"),
  FAIL((short) 5, "Không hoàn thành"),
  CANCEL((short)6, "Bị hủy");


  private Short value;
  private String name;


  RegisterEnum(Short value, String name) {
    this.value = value;
    this.name = name;
  }

  public short getValue() {
    return value;
  }

  public void setValue(Short value) {
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
