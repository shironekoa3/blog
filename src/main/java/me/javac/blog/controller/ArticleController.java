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
    public AjaxResult list(@RequestParam(required = false) Integer p,
                           @RequestParam(required = false) Integer size,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) String keyword) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        if (type == null || keyword == null) {
            type = "";
            keyword = "";
        }

        return AjaxResult.success(articleService.listAllArticle(p, size, type, keyword));
    }

    @GetMapping("/listHome")
    public AjaxResult listHome(@RequestParam(required = false) Integer p,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String keyword) {
        if (p == null || size == null) {
            p = 1;
            size = 10;
        }
        if (type == null) {
            type = "";
            keyword = "";
        }
        return AjaxResult.success(articleService.listHomeArticles(p, size, type, keyword));
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
