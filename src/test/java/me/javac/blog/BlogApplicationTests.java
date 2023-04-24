package me.javac.blog;

import lombok.extern.slf4j.Slf4j;
import me.javac.blog.controller.TagController;
import me.javac.blog.mapper.AccessMapper;
import me.javac.blog.service.IArticleService;
import me.javac.blog.service.ICategoryService;
import me.javac.blog.service.ILogininforService;
import me.javac.blog.service.ITagService;
import me.javac.blog.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Slf4j
class BlogApplicationTests {

    @Autowired
    TagController tagController;

    @Autowired
    ITagService tagService;

    @Autowired
    ICategoryService categoryService;


    @Autowired
    IArticleService articleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RedisCache redisCache;

    @Autowired
    ILogininforService logininforService;

    @Test
    void contextLoads() {

    }


}
