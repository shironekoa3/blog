package me.javac.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Comment;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.ICommentService;
import me.javac.blog.utils.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;


    @GetMapping(value = "/getQQInfo/{qq}")
    public AjaxResult getQQInfo(@PathVariable String qq) {
        return AjaxResult.success(commentService.getQQInfo(qq));
    }

    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable Long id) {
        return AjaxResult.success(commentService.getByArticleId(id));
    }

    @GetMapping("/list")
    public AjaxResult list(@RequestParam String searchKey) {
        return AjaxResult.success(commentService.listAllAndArticleTitle(searchKey));
    }
    @PostMapping(value = "/save")
    public AjaxResult save(@RequestBody Comment comment) {
        if (commentService.saveOrUpdate(comment)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult delete(@PathVariable Long id) {
        if (commentService.removeById(id)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

}
