package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.*;
import me.javac.blog.mapper.ArticleMapper;
import me.javac.blog.service.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    private final ArticleMapper articleMapper;
    private final ITagService tagService;
    private final ICategoryService categoryService;
    private final IArticleTagService articleTagService;

    private final IOptionService optionService;


    /**
     * 添加或修改文章。同时管理文章对应的标签或分类。
     *
     * @param entity 文章对象
     * @return 返回是否操作成功
     */
    @Override
    public boolean saveOrUpdate(Article entity) {
        // 判断分类是否存在（不存在则新增）
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getName, entity.getCategory().getName());
        Category tempCategory = categoryService.getOne(categoryLambdaQueryWrapper);
        if (tempCategory == null) {
            categoryService.save(entity.getCategory());
        } else {
            entity.setCategory(tempCategory);
        }
        entity.setCategoryId(entity.getCategory().getId());

        // 判断标签是否存在（不存在则新增）
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = null;
        for (int i = 0; i < entity.getTags().size(); i++) {
            tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagLambdaQueryWrapper.eq(Tag::getName, entity.getTags().get(i).getName());

            Tag tempTag = tagService.getOne(tagLambdaQueryWrapper);
            if (tempTag == null) {
                tagService.save(entity.getTags().get(i));
            } else {
                entity.getTags().set(i, tempTag);
            }
        }

        // 如果是更新文章，则先删除文章与标签的所有对应关系再添加，否则直接添加。
        if (entity.getId() != 0) {
            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, entity.getId());
            articleTagService.remove(articleTagLambdaQueryWrapper);
            super.updateById(entity);
        } else {
            super.save(entity);
            updateArticleCountOption();
        }

        // 添加文章与标签的对应关系
        for (Tag tag : entity.getTags()) {
            ArticleTag tempAt = new ArticleTag();
            tempAt.setArticleId(entity.getId());
            tempAt.setTagId(tag.getId());
            articleTagService.save(tempAt);
        }
        return true;
    }

    /**
     * 通过 ID 获取文章，同时获取文章分类及标签
     *
     * @param id 文章编号
     * @return 返回文章对象，失败则返回 null
     */
    @Override
    public Article getById(Serializable id) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId, id);
        IPage<Article> articleIPage = listPage(1, 1, articleLambdaQueryWrapper);
        if (articleIPage.getRecords().size() == 1) {
            // 访问量 + 1
            articleIPage.getRecords().get(0).setViewCount(articleIPage.getRecords().get(0).getViewCount() + 1);
            super.updateById(articleIPage.getRecords().get(0));
            return articleIPage.getRecords().get(0);
        }
        return null;
    }

    /**
     * 通过 ID 删除文章，同时删除文章与标签的对应关系
     *
     * @param id 文章编号
     * @return 返回是否操作成功
     */
    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        articleTagService.remove(articleTagLambdaQueryWrapper);
        boolean result = super.removeById(id);
        updateArticleCountOption();
        return result;
    }

    /**
     * 更新设置里的文章数量
     *
     * @return 返回是否操作成功
     */
    public boolean updateArticleCountOption() {
        Option o = new Option();
        o.setKey("articleCount");
        o.setValue(String.valueOf(count()));
        return optionService.updateOptionByKey(o);
    }


    /**
     * 分页查询文章
     *
     * @param pageNum            页编号
     * @param pageSize           页大小
     * @param lambdaQueryWrapper 查询条件
     * @return 返回文章分页对象
     */
    public IPage<Article> listPage(Integer pageNum, Integer pageSize, LambdaQueryWrapper<Article> lambdaQueryWrapper) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        Page<Article> articlePage = articleMapper.selectPage(page, lambdaQueryWrapper);

        for (int i = 0; i < articlePage.getRecords().size(); i++) {
            // 设置文章分类
            Category category = categoryService.getById(articlePage.getRecords().get(i).getCategoryId());
            articlePage.getRecords().get(i).setCategory(category);

            // 设置标签列表
            List<Tag> tagsByArticleId = tagService.getTagsByArticleId(articlePage.getRecords().get(i).getId());
            articlePage.getRecords().get(i).setTags(tagsByArticleId);
        }

        return articlePage;

    }

    /**
     * 获取首页文章
     *
     * @param pageNum  页编号
     * @param pageSize 页大小
     * @return 返回文章分页对象
     */
    @Override
    public IPage<Article> listHomeArticles(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(
                Article::getId, Article::getTitle, Article::getCreateTime,
                Article::getIsTop, Article::getCategoryId, Article::getViewCount,
                Article::getThumbnail, Article::getSummary);

        // 过滤隐藏
        articleLambdaQueryWrapper.eq(Article::getStatus, 0);

        // 排序
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        articleLambdaQueryWrapper.orderByDesc(Article::getCreateTime);
        return listPage(pageNum, pageSize, articleLambdaQueryWrapper);
    }


}
