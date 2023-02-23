package me.javac.blog.mapper;

import me.javac.blog.entity.Category;
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
public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> listAll();


}
