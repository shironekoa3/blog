package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.service.IUserService;
import me.javac.blog.utils.AjaxResult;
import me.javac.blog.vo.LoginVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/logout")
    public AjaxResult logout(String token) {
        if (!StringUtils.hasLength(token)) {
            return AjaxResult.error("token 不能为空！");
        }
        if (userService.logout(token)) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }

}
