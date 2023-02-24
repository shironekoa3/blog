package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.service.IArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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
    public Object list() {
        return ResponseEntity.ok(articleService.list());
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
