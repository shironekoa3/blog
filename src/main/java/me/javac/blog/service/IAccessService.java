package me.javac.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.javac.blog.entity.Access;
import me.javac.blog.vo.AccessStatisticalVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface IAccessService extends IService<Access> {

    boolean saveAccess(HttpServletRequest request);

    List<AccessStatisticalVo> statisticalByDays(Integer days);

}
