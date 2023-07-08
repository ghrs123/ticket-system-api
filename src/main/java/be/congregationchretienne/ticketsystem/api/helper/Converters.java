package be.congregationchretienne.ticketsystem.api.helper;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.*;

/** The {@code Converters} class contains static methods for registering Java Time converters. */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class Converters {

  /** The specific genericized type for {@code LocalDate}. */
  public static final Type LOCAL_DATE_TYPE = new TypeToken<LocalDate>() {}.getType();

  /** The specific genericized type for {@code LocalDateTime}. */
  public static final Type LOCAL_DATE_TIME_TYPE = new TypeToken<LocalDateTime>() {}.getType();

  /**
   * Registers all the Java Time converters.
   *
   * @param builder The GSON builder to register the converters with.
   * @return A reference to {@code builder}.
   */
  public static GsonBuilder registerAll(GsonBuilder builder) {
    if (builder == null) {
      throw new NullPointerException("builder cannot be null");
    }

    registerLocalDate(builder);
    registerLocalDateTime(builder);

    return builder;
  }

  /**
   * Registers the {@link LocalDateConverter} converter.
   *
   * @param builder The GSON builder to register the converter with.
   * @return A reference to {@code builder}.
   */
  public static GsonBuilder registerLocalDate(GsonBuilder builder) {
    builder.registerTypeAdapter(LOCAL_DATE_TYPE, new LocalDateConverter());

    return builder;
  }

  /**
   * Registers the {@link LocalDateTimeConverter} converter.
   *
   * @param builder The GSON builder to register the converter with.
   * @return A reference to {@code builder}.
   */
  public static GsonBuilder registerLocalDateTime(GsonBuilder builder) {
    builder.registerTypeAdapter(LOCAL_DATE_TIME_TYPE, new LocalDateTimeConverter());

    return builder;
  }
}
