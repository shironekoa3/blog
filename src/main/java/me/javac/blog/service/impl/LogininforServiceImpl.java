package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.javac.blog.entity.Logininfor;
import me.javac.blog.mapper.LogininforMapper;
import me.javac.blog.service.ILogininforService;
import me.javac.blog.utils.ip.AddressUtils;
import me.javac.blog.utils.ip.IpUtils;
import me.javac.blog.vo.LoginVo;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Service
public class LogininforServiceImpl extends ServiceImpl<LogininforMapper, Logininfor> implements ILogininforService {

    @Override
    public boolean saveLogininfor(HttpServletRequest request, LoginVo loginVo) {
        String userAgentString = request.getHeader("User-Agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(userAgentString);

        String ip = IpUtils.getIpAddr();
        String address = AddressUtils.getRealAddressByIP(ip);
        String os = c.os.family + " " + c.os.major;
        String browser = c.userAgent.family + " " +
                c.userAgent.major + "." + c.userAgent.minor + "." + c.userAgent.patch;


        Logininfor logininfor = new Logininfor();
        logininfor.setUsername(loginVo.getUsername());
        logininfor.setIpaddr(ip);
        logininfor.setLocation(address);
        logininfor.setBrowser(browser);
        logininfor.setOs(os);
        logininfor.setStatus(false);

        return super.save(logininfor);
    }

    @Override
    public Logininfor getLastRecord(String username) {
        LambdaQueryWrapper<Logininfor> logininforLambdaQueryWrapper = new LambdaQueryWrapper<>();
        logininforLambdaQueryWrapper.eq(Logininfor::getUsername, username);
        logininforLambdaQueryWrapper.orderByDesc(Logininfor::getCreateTime);
        logininforLambdaQueryWrapper.last("limit 1");
        return super.getOne(logininforLambdaQueryWrapper);
    }
}
