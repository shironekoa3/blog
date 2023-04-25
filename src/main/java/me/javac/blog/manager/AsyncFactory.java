package me.javac.blog.manager;

import me.javac.blog.entity.Option;
import me.javac.blog.service.IAccessService;
import me.javac.blog.service.IOptionService;
import me.javac.blog.utils.SpringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory {

    public static TimerTask recordVisitor(final HttpServletRequest request, final List<Option> optionList) {
        return new TimerTask() {
            @Override
            public void run() {
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
                    SpringUtils.getBean(IOptionService.class).updateById(viewCount);
                }

                // 记录信息
                SpringUtils.getBean(IAccessService.class).saveAccess(request);
            }
        };

    }

}