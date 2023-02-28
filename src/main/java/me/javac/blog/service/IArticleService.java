package me.javac.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import me.javac.blog.entity.Article;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface IArticleService extends IService<Article> {
    IPage<Article> listPage(Integer pageNum, Integer pageSize, LambdaQueryWrapper<Article> lambdaQueryWrapper);
    IPage<Article> listHomeArticles(Integer pageNum, Integer pageSize);

}
