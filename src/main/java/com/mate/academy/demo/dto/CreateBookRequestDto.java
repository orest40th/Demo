package com.mate.academy.demo.dto;

import com.mate.academy.demo.validation.Author;
import com.mate.academy.demo.validation.CoverImage;
import com.mate.academy.demo.validation.Description;
import com.mate.academy.demo.validation.Isbn;
import com.mate.academy.demo.validation.Title;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {
    @Title
    private String title;

    @Author
    private String author;

    @Isbn
    private String isbn;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Description
    private String description;

    @CoverImage
    private String coverImage;
}
