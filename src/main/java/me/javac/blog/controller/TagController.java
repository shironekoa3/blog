package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.ITagService;
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
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @GetMapping("/list")
    public Object list() {
        return ResponseEntity.ok(tagService.list());
    }

    @PostMapping("/change")
    public Object add(@RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.saveOrUpdate(tag));
    }

    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.removeById(id));
    }

}
