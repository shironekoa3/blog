package me.javac.blog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import me.javac.blog.controller.TagController;
import me.javac.blog.entity.Article;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.IArticleService;
import me.javac.blog.service.ICategoryService;
import me.javac.blog.service.ICommentService;
import me.javac.blog.service.ITagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

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


    @Resource
    ICommentService commentService;

    @Test
    void contextLoads() {

//        IPage<Article> articleIPage = articleService.listArticleSimplePage();
//        System.out.println(articleIPage.toString());

        String qqInfo = commentService.getQQInfo("1124240020");
        System.out.println(qqInfo);
    }


}
