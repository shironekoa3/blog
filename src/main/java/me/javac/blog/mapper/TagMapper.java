package me.javac.blog.mapper;

import me.javac.blog.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> listAll();

}
