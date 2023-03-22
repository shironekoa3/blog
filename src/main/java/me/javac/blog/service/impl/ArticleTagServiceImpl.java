package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.javac.blog.entity.ArticleTag;
import me.javac.blog.mapper.ArticleTagMapper;
import me.javac.blog.service.IArticleTagService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IArticleTagService {

}
