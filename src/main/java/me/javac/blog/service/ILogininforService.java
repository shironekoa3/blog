package me.javac.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.javac.blog.entity.Logininfor;
import me.javac.blog.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface ILogininforService extends IService<Logininfor> {

    boolean saveLogininfor(HttpServletRequest request, LoginVo loginVo);

    Logininfor getLastRecord(String username);

}
