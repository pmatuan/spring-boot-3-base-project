package org.base.shared.enumeration;

public enum ExampleEnum {
  UNKNOWN(0, "UNKNOWN"),
  EXAMPLE_ENUM(1, "example");

  private final int intValue;

  private final String stringValue;

  ExampleEnum(int intValue, String stringValue) {
    this.intValue = intValue;
    this.stringValue = stringValue;
  }

  public static ExampleEnum from(int value) {
    for (ExampleEnum exampleEnum : ExampleEnum.values()) {
      if (exampleEnum.intValue == value) {
        return exampleEnum;
      }
    }
    return UNKNOWN;
  }

  public static ExampleEnum from(String value) {
    for (ExampleEnum exampleEnum : ExampleEnum.values()) {
      if (exampleEnum.toString().equals(value)) {
        return exampleEnum;
      }
    }
    return UNKNOWN;
  }
}
