package io.igorv404.library.config.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  @NotNull
  @Length(min = 3)
  @Pattern(regexp = "^[A-Z]")
  private String title;

  @Column(nullable = false)
  @NotBlank
  @Pattern(regexp = "^[A-Z]\\S* [A-Z]\\S*$")
  private String author;

  @Column(nullable = false)
  @NotNull
  @Min(0)
  private Integer amount;
}
