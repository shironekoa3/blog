package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.entity.Category;
import me.javac.blog.entity.Option;
import me.javac.blog.mapper.ArticleMapper;
import me.javac.blog.mapper.CategoryMapper;
import me.javac.blog.service.ICategoryService;
import me.javac.blog.service.IOptionService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    private final ArticleMapper articleMapper;

    private final IOptionService optionService;

    public List<Category> listAndSearch(Wrapper<Category> queryWrapper) {
        List<Category> categoryList = super.list(queryWrapper);

        // 填充引用次数
        for (Category category : categoryList) {
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("category_id", category.getId());
            Long aLong = articleMapper.selectCount(articleQueryWrapper);
            category.setArticleCount(aLong);
        }
        return categoryList;
    }

    @Override
    public boolean saveOrUpdate(Category entity) {
        // 判断名称是否已经存在
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("name", entity.getName());
        Category tempCategory = getOne(categoryQueryWrapper);
        if (tempCategory != null) {
            return false;   // 存在同名则返回 false
        }

        boolean b = super.saveOrUpdate(entity);
        updateCategoryCountOption();
        return b;
    }

    @Override
    public boolean removeById(Serializable id) {
        // 判断是否有文章关联标签
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("category_id", id);
        if (articleMapper.selectCount(articleQueryWrapper) > 0) {
            return false;
        }
        boolean b = super.removeById(id);
        updateCategoryCountOption();
        return b;
    }

    /**
     * 更新设置里的分类数量
     *
     * @return 返回是否操作成功
     */
    public boolean updateCategoryCountOption() {
        Option o = new Option();
        o.setKey("categoryCount");
        o.setValue(String.valueOf(count()));
        return optionService.updateOptionByKey(o);
    }
}
