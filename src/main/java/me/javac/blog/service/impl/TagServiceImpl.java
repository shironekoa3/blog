package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.ArticleTag;
import me.javac.blog.entity.Option;
import me.javac.blog.entity.Tag;
import me.javac.blog.mapper.ArticleTagMapper;
import me.javac.blog.mapper.TagMapper;
import me.javac.blog.service.IOptionService;
import me.javac.blog.service.ITagService;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final ArticleTagMapper articleTagMapper;

    private final IOptionService optionService;

    public List<Tag> listAndSearch(Wrapper<Tag> queryWrapper) {
        List<Tag> tagList = super.list(queryWrapper);

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

        boolean b = super.saveOrUpdate(entity);
        updateTagCountOption();
        return b;
    }

    @Override
    public boolean removeById(Serializable id) {
        // 判断是否有文章关联标签
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("tag_id", id);
        if (articleTagMapper.selectCount(articleTagQueryWrapper) > 0) {
            return false;
        }
        boolean b = super.removeById(id);
        updateTagCountOption();
        return b;
    }

    /**
     * 通过文章ID获取所有标签
     *
     * @param id
     * @return
     */
    @Override
    public List<Tag> getTagsByArticleId(Long id) {
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagLambdaQueryWrapper);

        if (articleTagList.size() == 0) {
            return new ArrayList<>();
        }

        List<Long> tagIds = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagIds.add(articleTag.getTagId());
        }
        return super.listByIds(tagIds);
    }

    /**
     * 更新设置里的标签数量
     *
     * @return 返回是否操作成功
     */
    public boolean updateTagCountOption() {
        Option o = new Option();
        o.setKey("tagCount");
        o.setValue(String.valueOf(count()));
        return optionService.updateOptionByKey(o);
    }
}
