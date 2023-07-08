package be.congregationchretienne.ticketsystem.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ProblemDetail {

  @JsonProperty("name")
  private String name;

  @JsonProperty("reason")
  private String reason;

  @JsonProperty("value")
  private Object value;

  @JsonProperty("code")
  private String code;

  private ProblemDetail(Builder builder) {
    this.name = builder.name;
    this.reason = builder.reason;
    this.value = builder.value;
    this.code = builder.code;
  }

  public static class Builder {

    private String name;

    private String reason;

    private Object value;

    private String code;

    public static Builder newInstance() {
      return new Builder();
    }

    public Builder() {}

    public Builder name(String name) {
      this.name = name;

      return this;
    }

    public Builder reason(String reason) {
      this.reason = reason;

      return this;
    }

    public Builder status(Object value) {
      this.value = value;

      return this;
    }

    public Builder code(String code) {
      this.code = code;

      return this;
    }

    public ProblemDetail build() {

      return new ProblemDetail(this);
    }
  }
}
