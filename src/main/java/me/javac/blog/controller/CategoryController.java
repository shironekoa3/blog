package me.javac.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Category;
import me.javac.blog.service.ICategoryService;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/list")
    public AjaxResult listAndSearch(@RequestParam String searchKey) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = null;
        if(StringUtils.hasLength(searchKey)) {
            categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.like(Category::getName, searchKey);
            categoryLambdaQueryWrapper.or().like(Category::getDescription, searchKey);
        }
        return AjaxResult.success(categoryService.listAndSearch(categoryLambdaQueryWrapper));
    }

    @PostMapping("/change")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult add(@RequestBody Category category) {
        if (category.getName().equals("")) {
            return AjaxResult.error("分类名称不能为空！");
        }
        if (categoryService.saveOrUpdate(category)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult delete(@PathVariable Long id) {
        if (categoryService.removeById(id)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

}
