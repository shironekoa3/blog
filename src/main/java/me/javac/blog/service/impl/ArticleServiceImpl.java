package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.entity.ArticleTag;
import me.javac.blog.entity.Category;
import me.javac.blog.entity.Tag;
import me.javac.blog.mapper.ArticleMapper;
import me.javac.blog.mapper.ArticleTagMapper;
import me.javac.blog.mapper.CategoryMapper;
import me.javac.blog.mapper.TagMapper;
import me.javac.blog.service.IArticleService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private final TagMapper tagMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleTagMapper articleTagMapper;


    /**
     * 添加或修改文章。同时管理文章对应的标签或分类。
     *
     * @param entity
     * @return
     */
    @Override
    public boolean saveOrUpdate(Article entity) {
        // 判断分类是否存在（不存在则新增）
        QueryWrapper<Category> queryWrapperCategory = new QueryWrapper<>();
        queryWrapperCategory.eq("name", entity.getCategory().getName());
        Category tempCategory = categoryMapper.selectOne(queryWrapperCategory);
        if (tempCategory == null) {
            categoryMapper.insert(entity.getCategory());
        } else {
            entity.setCategory(tempCategory);
        }
        entity.setCategoryId(entity.getCategory().getId());

        // 判断标签是否存在（不存在则新增）
        QueryWrapper<Tag> queryWrapper = null;
        for (int i = 0; i < entity.getTags().size(); i++) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", entity.getTags().get(i).getName());

            Tag tempTag = tagMapper.selectOne(queryWrapper);
            if (tempTag == null) {
                tagMapper.insert(entity.getTags().get(i));
            } else {
                entity.getTags().set(i, tempTag);
            }
        }

        // 如果是更新文章，则先删除文章与标签的所有对应关系再添加，否则直接添加。
        if (entity.getId() != 0) {
            QueryWrapper<ArticleTag> queryWrapperArticleTag = new QueryWrapper<>();
            queryWrapperArticleTag.eq("article_id", entity.getId());
            articleTagMapper.delete(queryWrapperArticleTag);
            updateById(entity);
        } else {
            save(entity);
        }

        // 添加文章与标签的对应关系
        for (Tag tag : entity.getTags()) {
            ArticleTag tempAt = new ArticleTag();
            tempAt.setArticleId(entity.getId());
            tempAt.setTagId(tag.getId());
            articleTagMapper.insert(tempAt);
        }
        return true;
    }


    /**
     * 获取所有文章，包括文章分类和标签。
     *
     * @return
     */
    @Override
    public List<Article> list() {
        List<Article> articleList = super.list();

        for (int i = 0; i < articleList.size(); i++) {
            // 给文章设置分类
            Category tempCategory = categoryMapper.selectById(articleList.get(i).getCategoryId());
            articleList.get(i).setCategory(tempCategory);

            // 给文章设置标签
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            articleTagQueryWrapper.eq("article_id", articleList.get(i).getId());
            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagQueryWrapper);
            List<Tag> tagList = new ArrayList<>();
            for (ArticleTag articleTag : articleTagList) {
                Tag tempTag = tagMapper.selectById(articleTag.getTagId());
                tagList.add(tempTag);
            }

            articleList.get(i).setTags(tagList);
        }

        return articleList;
    }

    /**
     * 通过 ID 获取文章，同时获取文章分类及标签
     *
     * @param id
     * @return
     */
    @Override
    public Article getById(Serializable id) {
        Article article = super.getById(id);

        // 获取分类
        article.setCategory(categoryMapper.selectById(article.getCategoryId()));

        // 获取标签
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("article_id", id);
        List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagQueryWrapper);
        List<Tag> tagList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            Tag tempTag = tagMapper.selectById(articleTag.getTagId());
            tagList.add(tempTag);
        }
        article.setTags(tagList);
        return article;
    }

    /**
     * 通过 ID 删除文章，同时删除文章与标签的对应关系
     *
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("article_id", id);
        articleTagMapper.delete(articleTagQueryWrapper);
        return super.removeById(id);
    }
}
