package com.mate.academy.demo.dto;

import com.mate.academy.demo.validation.Author;
import com.mate.academy.demo.validation.CoverImage;
import com.mate.academy.demo.validation.Description;
import com.mate.academy.demo.validation.Title;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateBookRequestDto {
    @Title
    @Length(min = 2, max = 100)
    private String title;

    @Author
    @Length(max = 50)
    private String author;

    @ISBN(type = ISBN.Type.ISBN_13)
    private String isbn;

    @Positive
    private BigDecimal price;

    @Description
    @Length(min = 2, max = 1000)
    private String description;

    @CoverImage
    private String coverImage;

    private Set<Long> categoryIds;
}
