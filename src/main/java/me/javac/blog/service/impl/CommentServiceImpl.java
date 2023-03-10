package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.entity.Comment;
import me.javac.blog.mapper.CommentMapper;
import me.javac.blog.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    private final RestTemplate restTemplate;

    @Override
    public List<Comment> getByArticleId(Long id) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, id);
        return super.list(commentLambdaQueryWrapper);
    }

    @Override
    public String getQQInfo(String qq) {
        String uri = "https://api.usuuu.com/qq/" + qq;
        return restTemplate.getForObject(uri, String.class);
    }


}
