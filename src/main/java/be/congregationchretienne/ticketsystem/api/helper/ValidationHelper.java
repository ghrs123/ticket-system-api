package be.congregationchretienne.ticketsystem.api.helper;

import org.apache.commons.lang3.StringUtils;

public class ValidationHelper {

  private ValidationHelper() {}

  public static boolean isNotBlank(String value) {
    return StringUtils.isNotBlank(value);
  }

  public static boolean requireNonNull(Object value) {
    if (value == null) {

      throw new IllegalArgumentException("Parameter must be not null.");
    }

    return false;
  }

  public static boolean requireNonBlank(String value) {
    requireNonNull(value);

    if (!isNotBlank(value)) {

      throw new IllegalArgumentException("Parameter must be not blank.");
    }

    return false;
  }

  public static boolean isNotNull(Object value) {
    return value != null;
  }
}
