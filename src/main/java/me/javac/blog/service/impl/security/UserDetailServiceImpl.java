package me.javac.blog.service.impl.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.service.IUserService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<me.javac.blog.entity.User> queryWrapperUsername = new LambdaQueryWrapper<>();
        queryWrapperUsername.eq(me.javac.blog.entity.User::getUsername, username);
        me.javac.blog.entity.User user = userService.getOne(queryWrapperUsername);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }

        return new User(user.getUsername(), user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getIdentity()));
    }
}
