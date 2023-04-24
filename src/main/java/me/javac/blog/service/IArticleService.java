package me.javac.blog.service;

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
    IPage<Article> listAllArticle(Integer pageNum, Integer pageSize, String type, String keyword);
    IPage<Article> listHomeArticles(Integer pageNum, Integer pageSize, String type, String keyword);

}
