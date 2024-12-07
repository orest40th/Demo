package com.mate.academy.demo;

import com.mate.academy.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book javaBook = new Book();
                javaBook.setAuthor("user userowsky");
                javaBook.setDescription("java coding reference");
                javaBook.setTitle("Coding in Java");
                javaBook.setIsbn("978-3-16-148410-0");
                javaBook.setPrice(BigDecimal.TEN);
                javaBook.setCoverImage("jvrf.png");

                bookService.save(javaBook);
                System.out.println(bookService.findAll());
            }
        };
    }*/
}
