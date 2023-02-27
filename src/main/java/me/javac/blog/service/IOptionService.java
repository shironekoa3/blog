package me.javac.blog.service;

import me.javac.blog.entity.Option;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface IOptionService extends IService<Option> {
    boolean updateByKey(String key, String value);

    boolean addViewCount();

}
