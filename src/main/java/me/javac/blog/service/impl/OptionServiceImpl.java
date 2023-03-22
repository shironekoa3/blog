package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Option;
import me.javac.blog.mapper.OptionMapper;
import me.javac.blog.service.IOptionService;
import org.springframework.stereotype.Service;

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
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements IOptionService {

    @Override
    public boolean updateOptionByKey(Option option) {
        LambdaQueryWrapper<Option> optionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        optionLambdaQueryWrapper.eq(Option::getKey, option.getKey());
        Option sourceOption = super.getOne(optionLambdaQueryWrapper);
        if (sourceOption != null) {
            sourceOption.setValue(option.getValue());
            sourceOption.setUpdateTime(null);
            return super.updateById(sourceOption);
        }
        return false;
    }

    @Override
    public boolean updateOptions(List<Option> optionList) {
        List<Option> sourceOptionList = super.list();
        for (Option option : optionList) {
            for (Option sourceOption : sourceOptionList) {
                if (sourceOption.getKey().equals(option.getKey())) {
                    if (!sourceOption.getValue().equals(option.getValue())) {
                        sourceOption.setUpdateTime(null);
                        sourceOption.setValue(option.getValue());
                        super.updateById(sourceOption);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public List<Option> listHomePageOptions() {
        List<Option> optionList = super.list();

        // 访问量 + 1
        Option viewCount = null;
        for (Option option : optionList) {
            if ("viewCount".equals(option.getKey())) {
                viewCount = option;
                break;
            }
        }
        if (viewCount != null) {
            viewCount.setValue(String.valueOf(Integer.parseInt(viewCount.getValue()) + 1));
            super.updateById(viewCount);
        }
        return optionList;
    }

}
