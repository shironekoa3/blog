package me.javac.blog.service;

import me.javac.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface ICommentService extends IService<Comment> {

    List<Comment> getByArticleId(Long id);

    String getQQInfo(String qq);

}
