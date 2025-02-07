package com.mate.academy.demo.dto;

import com.mate.academy.demo.validation.Author;
import com.mate.academy.demo.validation.CoverImage;
import com.mate.academy.demo.validation.Description;
import com.mate.academy.demo.validation.Title;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;

@Getter
@Setter
public class CreateBookRequestDto {
    @Title
    private String title;

    @Author
    private String author;

    @ISBN(type = ISBN.Type.ISBN_13)
    private String isbn;

    @Positive
    private BigDecimal price;

    @Description
    private String description;

    @CoverImage
    private String coverImage;
}
