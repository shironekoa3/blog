package me.javac.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.javac.blog.entity.Tag;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface ITagService extends IService<Tag> {

    List<Tag> getTagsByArticleId(Long id);

}
