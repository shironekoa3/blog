package me.javac.blog;

import lombok.extern.slf4j.Slf4j;
import me.javac.blog.controller.TagController;
import me.javac.blog.service.ICategoryService;
import me.javac.blog.service.ITagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BlogApplicationTests {

    @Autowired
    TagController tagController;

    @Autowired
    ITagService tagService;

    @Autowired
    ICategoryService categoryService;

    @Test
    void contextLoads() {


    }


}
