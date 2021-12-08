package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum ClassStatusEnum implements IEnum {

  CHO_DANG_KY_LOP((short) 1, "Chờ Đăng Ký"),
  DA_DU_SO_LUONG((short) 2, "Đã Đủ Số Lượng"),
  XAC_NHAN((short) 3, "Xác nhận"),
  HUY_LOP((short) 4, "Hủy lớp");

  private short value;
  private String name;

  ClassStatusEnum(short value, String name) {
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
