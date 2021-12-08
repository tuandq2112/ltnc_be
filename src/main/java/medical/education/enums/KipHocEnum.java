package medical.education.enums;

import spring.backend.library.enums.IEnum;

public enum KipHocEnum implements IEnum {
    KIP1((short) 1, "07:00 - 09:00"),
    KIP2((short) 2, "09:00 - 11:00"),
    KIP3((short) 3, "12:00 - 15:00"),
    KIP4((short) 4, "15:00 - 17:00");

    private short value;
    private String name;

    KipHocEnum(short value, String name) {
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
