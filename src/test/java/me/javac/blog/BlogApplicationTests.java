package me.javac.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.javac.blog.controller.TagController;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.ICategoryService;
import me.javac.blog.service.ITagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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

    Tag newTag(String name) {
        Tag t = new Tag();
        t.setName(name);
        return t;
    }

    @Test
    void contextLoads() {

        List<Tag> tags = Arrays.asList(
                newTag("A"),
                newTag("C"),
                newTag("E")
        );

        QueryWrapper<Tag> queryWrapper = null;
        for (int i = 0; i < tags.size(); i++) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", tags.get(i).getName());

            Tag tempTag = tagService.getOne(queryWrapper);
            if (tempTag == null) {
                tagService.save(tags.get(i));
            } else {
                tags.set(i, tempTag);
            }
        }

        System.out.println();
//        tagService.saveBatch(tags);

        tags.forEach(System.out::println);
    }


}
