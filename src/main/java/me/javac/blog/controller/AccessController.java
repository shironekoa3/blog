package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.service.IAccessService;
import me.javac.blog.utils.AjaxResult;
import me.javac.blog.vo.AccessStatisticalVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/access")
public class AccessController {

    private final IAccessService accessService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('admin')")
    public AjaxResult list(Integer days) {
        if (days == null || days == 0) {
            days = 7;
        }

        List<AccessStatisticalVo> accessStatisticalVos = accessService.statisticalByDays(days);

        Map<String, Integer> result = new HashMap<>();

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = days - 1; i > 0; --i) {
            String dateText = currentDate.minusDays(i).format(formatter);
            result.put(dateText, 0);
        }

        accessStatisticalVos.forEach(i -> result.put(i.getDate(), i.getCount()));

        return AjaxResult.success(result);
    }


}
