package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.service.IArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleService articleService;

    @GetMapping("/get/{id}")
    public Object get(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @GetMapping("/list")
    public Object list(@RequestParam Integer p, @RequestParam Integer size) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        return ResponseEntity.ok(articleService.listPage(p, size, null));
    }

    @GetMapping("/listHome")
    public Object listHome(@RequestParam Integer p, @RequestParam Integer size) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        return ResponseEntity.ok(articleService.listHomeArticles(p, size));
    }

    @PostMapping("/change")
    public Object change(@RequestBody Article article) {
        return ResponseEntity.ok(articleService.saveOrUpdate(article));
    }

    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.removeById(id));
    }

}
