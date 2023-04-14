package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Comment;
import me.javac.blog.mapper.CommentMapper;
import me.javac.blog.service.ICommentService;
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

    private final CommentMapper commentMapper;

    @Override
    public List<Comment> getByArticleId(Long id) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, id);
        return super.list(commentLambdaQueryWrapper);
    }

    @Override
    public JsonNode getQQInfo(String qq) {
        String uri = "https://api.usuuu.com/qq/" + qq;
        String jsonStringResult = restTemplate.getForObject(uri, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(jsonStringResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comment> listAllAndArticleTitle(String searchKey) {
        return commentMapper.selectAllAndArticleTitle(searchKey == null ? "" : searchKey);
    }


}
