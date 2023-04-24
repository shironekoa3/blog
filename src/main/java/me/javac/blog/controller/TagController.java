package me.javac.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.ITagService;
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
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) String searchKey) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = null;
        if (StringUtils.hasLength(searchKey)) {
            tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagLambdaQueryWrapper.like(Tag::getName, searchKey);
            tagLambdaQueryWrapper.or().like(Tag::getDescription, searchKey);
        }
        return AjaxResult.success(tagService.listAndSearch(tagLambdaQueryWrapper));
    }

    @PostMapping("/change")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult add(@RequestBody Tag tag) {
        if (tag.getName().equals("")) {
            return AjaxResult.error("标签名称不能为空！");
        }
        if (tagService.saveOrUpdate(tag)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult delete(@PathVariable Long id) {
        if (tagService.removeById(id)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

}
