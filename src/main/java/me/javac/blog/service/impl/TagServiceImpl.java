package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Article;
import me.javac.blog.entity.ArticleTag;
import me.javac.blog.entity.Category;
import me.javac.blog.entity.Tag;
import me.javac.blog.mapper.ArticleTagMapper;
import me.javac.blog.mapper.TagMapper;
import me.javac.blog.service.IArticleTagService;
import me.javac.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final ArticleTagMapper articleTagMapper;

    @Override
    public List<Tag> list() {
        List<Tag> tagList = super.list();

        // 填充引用次数
        for (Tag tag : tagList) {
            QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
            articleTagQueryWrapper.eq("tag_id", tag.getId());
            Long aLong = articleTagMapper.selectCount(articleTagQueryWrapper);
            tag.setRefCount(aLong);
        }
        return tagList;
    }


    @Override
    public boolean saveOrUpdate(Tag entity) {
        // 判断名称是否已经存在
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("name", entity.getName());
        Tag tempTag = getOne(tagQueryWrapper);
        if (tempTag != null) {
            return false;   // 存在同名则返回 false
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        // 判断是否有文章关联标签
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("tag_id", id);
        if (articleTagMapper.selectCount(articleTagQueryWrapper) > 0) {
            return false;
        }
        return super.removeById(id);
    }
}
