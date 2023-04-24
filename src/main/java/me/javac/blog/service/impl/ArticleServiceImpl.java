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
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper;
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
            ArticleTag tempAt = new ArticleTag(entity.getId(), tag.getId());
            articleTagService.save(tempAt);
        }

        // Lambda 表达式写法：
//        List<ArticleTag> collect = entity.getTags()
//                .stream().map(tag -> new ArticleTag(entity.getId(), tag.getId()))
//                .collect(Collectors.toList());
//        articleTagService.saveBatch(collect);
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
     * 分页查询文章所有数据
     *
     * @param pageNum                   页编号
     * @param pageSize                  页大小
     * @param articleLambdaQueryWrapper 查询条件
     * @return 文章列表
     */
    public IPage<Article> listPage(Integer pageNum, Integer pageSize, LambdaQueryWrapper<Article> articleLambdaQueryWrapper) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);


        Page<Article> articlePage = articleMapper.selectPage(page, articleLambdaQueryWrapper);

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
     * 获取后台管理所有文章数据
     *
     * @param pageNum 页码
     * @param pageSize 页尺寸
     * @param type 搜索类型（标题、分类、标签）
     * @param keyword 搜索关键字
     * @return 返回符合条件的文章列表
     */
    @Override
    public IPage<Article> listAllArticle(Integer pageNum, Integer pageSize, String type, String keyword) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = null;
        if (StringUtils.hasLength(type) && StringUtils.hasLength(keyword)) {
            lambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (type.equals("title")) {
                lambdaQueryWrapper.like(Article::getTitle, keyword);
            }
        }

        return listPage(pageNum, pageSize, lambdaQueryWrapper);
    }

    /**
     * 获取首页文章数据，删除了隐藏文章
     *
     * @param pageNum 页码
     * @param pageSize 页尺寸
     * @param type 搜索类型（标题、分类、标签）
     * @param keyword 搜索关键字
     * @return 返回符合条件的文章列表
     */
    @Override
    public IPage<Article> listHomeArticles(Integer pageNum, Integer pageSize, String type, String keyword) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(
                Article::getId, Article::getTitle, Article::getCreateTime,
                Article::getIsTop, Article::getCategoryId, Article::getViewCount,
                Article::getThumbnail, Article::getSummary);

        // 过滤隐藏
        articleLambdaQueryWrapper.eq(Article::getStatus, 0);

        // 搜索
        if ("category".equals(type)) {
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
            categoryLambdaQueryWrapper.eq(Category::getName, keyword);
            Category category = categoryService.getOne(categoryLambdaQueryWrapper);
            if (category != null && category.getId() != 0) {
                articleLambdaQueryWrapper.eq(Article::getCategoryId, category.getId());
            }
        } else if ("tag".equals(type)) {
            LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagLambdaQueryWrapper.eq(Tag::getName, keyword);
            Tag tag = tagService.getOne(tagLambdaQueryWrapper);
            if (tag != null && tag.getId() != 0) {
                // 查询包含此标签的所有文章
                LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
                articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, tag.getId());
                List<ArticleTag> articleTagList = articleTagService.list(articleTagLambdaQueryWrapper);

                List<Long> articleIds = articleTagList.stream()
                        .map(ArticleTag::getArticleId).collect(Collectors.toList());

                if (articleIds.size() == 0) {
                    articleIds.add(0L);
                }
                articleLambdaQueryWrapper.in(Article::getId, articleIds);
            }
        }


        // 排序
        articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
        articleLambdaQueryWrapper.orderByDesc(Article::getCreateTime);
        return listPage(pageNum, pageSize, articleLambdaQueryWrapper);
    }

}
