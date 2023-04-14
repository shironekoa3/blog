package me.javac.blog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import me.javac.blog.entity.Category;
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
public interface ICategoryService extends IService<Category> {
    List<Category> listAndSearch(Wrapper<Category> queryWrapper);
}
