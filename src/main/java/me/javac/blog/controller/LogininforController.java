package me.javac.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Logininfor;
import me.javac.blog.service.ILogininforService;
import me.javac.blog.utils.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/logininfor")
@RequiredArgsConstructor
public class LogininforController {

    private final ILogininforService logininforService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult list() {
        LambdaQueryWrapper<Logininfor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Logininfor::getCreateTime);
        lambdaQueryWrapper.last("limit 30");
        return AjaxResult.success(logininforService.list(lambdaQueryWrapper));
    }

}
