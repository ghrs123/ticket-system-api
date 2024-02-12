package be.congregationchretienne.ticketsystem.api.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.congregationchretienne.ticketsystem.api.exception.IllegalArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
class ValidationHelperTest {

  @Test
  void notBlankIsTrue() {

    // give
    String text = "Test if is not blank.";
    // when
    Boolean isNotBlank = ValidationHelper.isNotBlank(text);
    // then
    assertThat(isNotBlank).isEqualTo(true);
  }

  @Test
  void notBlankIsFalse() {

    // give
    String text = "";
    // when
    Boolean isNotBlank = ValidationHelper.isNotBlank(text);
    // then
    assertThat(isNotBlank).isEqualTo(false);
  }

  @Test
  void objectRequireNonNull() {
    // give
    Object object = new Object();
    // when
    boolean isNotNull = ValidationHelper.requireNonNull(object);
    // then
    assertThat(isNotNull).isEqualTo(false);
  }

  @Test
  void objectRequireIsNull() {
    // give
    Object object = null;
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonNull(object);
            });
    // then
    Assertions.assertEquals("Parameter must be not null.", exception.getMessage());
  }

  @Test
  void requireNonBlankFalse() {
    // give
    String text = "Test if is require non blank.";
    // when
    boolean isBlank = ValidationHelper.requireNonBlank(text);
    // then
    assertThat(isBlank).isEqualTo(false);
  }

  @Test
  void requireNonBlankTrue() {
    // give
    String text = "";
    // when
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              ValidationHelper.requireNonBlank(text);
            });
    // then
    Assertions.assertEquals("Parameter must be not blank.", exception.getMessage());
  }

  @Test
  void isNotNullTrue() {
    // give
    Object object = new Object();
    // when
    boolean objectsNotNull = ValidationHelper.isNotNull(object);
    // then
    assertThat(objectsNotNull).isEqualTo(true);
  }

  @Test
  void isNotNullFalse() {
    // give
    Object object = null;
    // when
    boolean objectsNotNull = ValidationHelper.isNotNull(object);
    // then
    assertThat(objectsNotNull).isEqualTo(false);
  }
}
