package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Comment;
import me.javac.blog.service.ICommentService;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;


    @GetMapping(value = "/getQQInfo/{qq}")
    public Object getQQInfo(@PathVariable String qq) {
        return ResponseEntity.ok(commentService.getQQInfo(qq));
    }

    @GetMapping("/get/{id}")
    public Object get(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getByArticleId(id));
    }

    @PostMapping(value = "/save")
    public Object save(@RequestBody Comment comment){
        return ResponseEntity.ok(commentService.saveOrUpdate(comment));
    }

}
