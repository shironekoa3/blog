package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Category;
import me.javac.blog.service.ICategoryService;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/list")
    public Object list() {
        return ResponseEntity.ok(categoryService.list());
    }

    @PostMapping("/change")
    public Object add(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.saveOrUpdate(category));
    }

    @GetMapping("/delete/{id}")
    public Object delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.removeById(id));
    }

}
