package io.igorv404.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;
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
  @NotNull(message = "Value can't be null")
  @Length(min = 3, message = "Value must be longer than 3")
  @Pattern(regexp = "^[A-Z].*$", message = "Value must starts with capital letter")
  private String title;

  @Column(nullable = false)
  @NotBlank(message = "Value can't be null or empty")
  @Pattern(regexp = "^[A-Z]\\S* [A-Z]\\S*$", message = "Value must be as two words which starts with capital letter")
  private String author;

  @Column(nullable = false)
  @NotNull(message = "Value can't be null")
  @Min(value = 0, message = "Value must be equal or bigger than 0")
  private Integer amount;
}
