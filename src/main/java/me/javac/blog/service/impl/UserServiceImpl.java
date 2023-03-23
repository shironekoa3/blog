package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Logininfor;
import me.javac.blog.entity.User;
import me.javac.blog.mapper.UserMapper;
import me.javac.blog.service.ILogininforService;
import me.javac.blog.service.IUserService;
import me.javac.blog.utils.JwtUtil;
import me.javac.blog.vo.LoginVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final AuthenticationManager authenticationManager;

    private final ILogininforService logininforService;

    private final PasswordEncoder passwordEncoder;

    public boolean createAdminUser(LoginVo loginVo) {
        User user = new User();
        user.setUsername(loginVo.getUsername());
        user.setPassword(passwordEncoder.encode(loginVo.getPassword()));
        user.setIdentity("admin");
        return super.save(user);
    }

    @Override
    public String login(HttpServletRequest request, LoginVo loginVo) {
        if (super.count() == 0) {
            createAdminUser(loginVo);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());

        // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (authentication == null) {
            throw new RuntimeException("登陆失败！");
        }

        // 保存登录记录
        logininforService.saveLogininfor(request, loginVo);

        // 如果认证通过，使用 userId 生成一个 JWT
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return JwtUtil.createJWT(user.getUsername());
    }

    @Override
    public boolean logout(String token) {
        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 获取用户名
        String username = claims.getSubject();

        // 能进入此方法说明登陆并没有过期。只需要把最后一次登陆记录的状态改为 1 即可判断手动登出。
        // SELECT * FROM logininfor WHERE username='xxx' ORDER BY update_time DESC
        LambdaQueryWrapper<Logininfor> logininforLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logininforLambdaQueryWrapper.eq(Logininfor::getUsername, username);
        logininforLambdaQueryWrapper.eq(Logininfor::getStatus, 0);
        logininforLambdaQueryWrapper.orderByDesc(Logininfor::getCreateTime);

        Logininfor logininfor = logininforService.getOne(logininforLambdaQueryWrapper, false);

        if (logininfor == null) {
            return false;
        }

        logininfor.setStatus(true);
        logininfor.setUpdateTime(null);
        return logininforService.updateById(logininfor);
    }

}
