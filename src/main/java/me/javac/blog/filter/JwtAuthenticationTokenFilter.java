package me.javac.blog.filter;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Logininfor;
import me.javac.blog.entity.User;
import me.javac.blog.exception.ServiceException;
import me.javac.blog.mapper.UserMapper;
import me.javac.blog.service.ILogininforService;
import me.javac.blog.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final ILogininforService logininforService;

    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        SecurityContextHolder.clearContext();

        // 获取 Token
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            // token 过期，或非法。提示重新登陆。
            throw new ServiceException("当前登陆已过期！", HttpStatus.UNAUTHORIZED.value());
        }

        String username = claims.getSubject();

        Logininfor lastRecord = logininforService.getLastRecord(username);
        if (lastRecord == null) {
            throw new ServiceException("当前登陆无效！", HttpStatus.UNAUTHORIZED.value());
        }

        if (lastRecord.getStatus()) {
            throw new ServiceException("请重新登陆！", HttpStatus.UNAUTHORIZED.value());
        }

        if (lastRecord.getCreateTime().plusSeconds(JwtUtil.JWT_TTL).isBefore(LocalDateTime.now())) {
            throw new ServiceException("当前登陆已过期！", HttpStatus.UNAUTHORIZED.value());
        }

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(user.getIdentity()));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }
}
