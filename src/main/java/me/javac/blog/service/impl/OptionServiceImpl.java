package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Option;
import me.javac.blog.mapper.OptionMapper;
import me.javac.blog.service.IOptionService;
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
@RequiredArgsConstructor
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements IOptionService {

    private final OptionMapper optionMapper;

    @Override
    public boolean updateByKey(String key, String value) {
        UpdateWrapper<Option> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("`key`", key);
        Option o = new Option();
        o.setValue(value);
        return optionMapper.update(o, updateWrapper) == 1;
    }

    @Override
    public boolean addViewCount() {
        QueryWrapper<Option> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", "viewCount");
        Option option = optionMapper.selectOne(queryWrapper);
        option.setValue(String.valueOf(Integer.parseInt(option.getValue()) + 1));
        return optionMapper.updateById(option) == 1;
    }
}
