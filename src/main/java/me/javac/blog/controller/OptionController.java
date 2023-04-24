package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Option;
import me.javac.blog.service.IAccessService;
import me.javac.blog.service.IOptionService;
import me.javac.blog.utils.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/option")
@RequiredArgsConstructor
public class OptionController {

    private final IOptionService optionService;
    private final IAccessService accessService;


    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(optionService.list());
    }

    @GetMapping("/listHome")
    public AjaxResult listHome(HttpServletRequest request) {
        // 记录访客信息
        accessService.saveAccess(request);
        return AjaxResult.success(optionService.listHomePageOptions());
    }

    @PostMapping("/change")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult change(@RequestBody List<Option> optionList) {
        if (optionService.updateOptions(optionList)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }

    }

}
