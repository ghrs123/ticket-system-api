package be.congregationchretienne.ticketsystem.api.error;

import be.congregationchretienne.ticketsystem.api.helper.ValidationHelper;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Setter
@Getter
public final class Problem {

  private String eventId;

  private String title;

  private String message;

  private HttpStatus status;

  private String path;

  private List<ProblemDetail> invalidParameters;

  private Problem(ProblemBuilder builder) {
    this.eventId = builder.eventId;
    this.title = builder.title;
    this.message = builder.message;
    this.status = builder.status;
    this.path = builder.path;

    if (invalidParameters.isEmpty()) this.invalidParameters = new ArrayList<>();
  }

  public static class ProblemBuilder {

    private String eventId;

    private String title;

    private String message;

    private HttpStatus status;

    private String path;

    private List<ProblemDetail> invalidParameters;

    public static ProblemBuilder newInstance() {
      return new ProblemBuilder();
    }

    public ProblemBuilder() {}

    public ProblemBuilder eventId(String eventId) {
      this.eventId = eventId;

      return this;
    }

    public ProblemBuilder title(String title) {
      this.title = title;

      return this;
    }

    public ProblemBuilder message(String message) {
      this.message = message;

      return this;
    }

    public ProblemBuilder status(HttpStatus status) {
      this.status = status;

      return this;
    }

    public ProblemBuilder path(String path) {
      this.path = path;

      return this;
    }

    public ProblemBuilder invalidParameters(ProblemDetail invalidParameters) {
      ValidationHelper.requireNonNull(invalidParameters);
      this.invalidParameters.add(invalidParameters);

      return this;
    }

    // build method to deal with outer class
    // to return outer instance
    public Problem build() {

      return new Problem(this);
    }
  }
}
