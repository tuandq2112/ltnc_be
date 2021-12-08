package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum StudyProcessEnum implements IEnum {

  TOT((short) 1, "Tốt"),
  KHA((short) 2, "Khá"),
  TRUNG_BINH((short) 3, "Trung Bình"),
  TRUOT((short) 4, "Trượt"),
  DANG_HOC((short) 5, "Đang học"),
  DINH_CHI((short) 6, "Đình chỉ");

  public short value;
  public String name;

  StudyProcessEnum(short value, String name) {
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
