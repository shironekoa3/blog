package me.javac.blog.mapper;

import me.javac.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*,a.title as articleTitle FROM `comment` AS c, article AS a WHERE c.`article_id`=a.`id` AND c.`is_deleted`=0")
    List<Comment> selectAllAndArticleTitle();

}
