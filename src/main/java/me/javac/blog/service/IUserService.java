package me.javac.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.javac.blog.entity.User;
import me.javac.blog.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface IUserService extends IService<User> {

    String login(HttpServletRequest request, LoginVo loginVo);

    boolean logout(String token);

}
