package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.service.IArticleService;
import me.javac.blog.utils.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public AjaxResult get(@PathVariable Long id) {
        return AjaxResult.success(articleService.getById(id));
    }

    @GetMapping("/list")
    public AjaxResult list(@RequestParam Integer p, @RequestParam Integer size) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        return AjaxResult.success(articleService.listPage(p, size, null));
    }

    @GetMapping("/listHome")
    public AjaxResult listHome(@RequestParam Integer p, @RequestParam Integer size) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        return AjaxResult.success(articleService.listHomeArticles(p, size));
    }

    @PostMapping("/change")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult change(@RequestBody Article article) {
        if (articleService.saveOrUpdate(article)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult delete(@PathVariable Long id) {
        if (articleService.removeById(id)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

}
