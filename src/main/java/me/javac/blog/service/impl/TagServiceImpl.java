package me.javac.blog.service.impl;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Tag;
import me.javac.blog.mapper.TagMapper;
import me.javac.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    private final TagMapper tagMapper;

    @Override
    public List<Tag> list() {
        return tagMapper.listAll();
    }

}
