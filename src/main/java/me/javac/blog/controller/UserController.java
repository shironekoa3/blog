package me.javac.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.User;
import me.javac.blog.service.IUserService;
import me.javac.blog.utils.AjaxResult;
import me.javac.blog.utils.JwtUtil;
import me.javac.blog.vo.LoginVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @PostMapping(value = "/login")
    public AjaxResult login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        if (!StringUtils.hasLength(loginVo.getUsername())
                || !StringUtils.hasLength(loginVo.getPassword())) {
            return AjaxResult.error("用户名或密码不能为空！");
        }

        String loginToken = userService.login(request, loginVo);

        Map<String, String> result = new HashMap<>();
        result.put("token", loginToken);

        return AjaxResult.success(result);
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            return AjaxResult.error("token 不能为空！");
        }
        if (userService.logout(token)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/reset")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult reset(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            return AjaxResult.error("token 不能为空！");
        }

        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 获取用户名
        String username = claims.getSubject();

        // 退出登陆
        if (userService.logout(token)) {
            // 删除用户
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getUsername, username);
            if (userService.remove(userLambdaQueryWrapper)) {
                return AjaxResult.success();
            } else {
                return AjaxResult.error("删除账户失败！");
            }
        } else {
            return AjaxResult.error("退出登陆失败！");
        }
    }
}
