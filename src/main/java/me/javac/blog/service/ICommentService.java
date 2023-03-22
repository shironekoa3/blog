package me.javac.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.JsonNode;
import me.javac.blog.entity.Comment;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> getByArticleId(Long id);

    JsonNode getQQInfo(String qq);

    List<Comment> listAllAndArticleTitle();

}
