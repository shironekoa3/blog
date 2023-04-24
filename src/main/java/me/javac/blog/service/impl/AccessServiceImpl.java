package me.javac.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Access;
import me.javac.blog.mapper.AccessMapper;
import me.javac.blog.service.IAccessService;
import me.javac.blog.utils.ip.AddressUtils;
import me.javac.blog.utils.ip.IpUtils;
import me.javac.blog.vo.AccessStatisticalVo;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
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
public class AccessServiceImpl extends ServiceImpl<AccessMapper, Access> implements IAccessService {

    private final AccessMapper accessMapper;

    public boolean saveAccess(HttpServletRequest request) {
        String userAgentString = request.getHeader("User-Agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(userAgentString);

        String ip = IpUtils.getIpAddr();
        String address = AddressUtils.getRealAddressByIP(ip);
        String os = c.os.family + " " + c.os.major;
        String browser = c.userAgent.family + " " +
                c.userAgent.major + "." + c.userAgent.minor + "." + c.userAgent.patch;

        Access access = new Access();
        access.setIpaddr(ip);
        access.setLocation(address);
        access.setBrowser(browser);
        access.setOs(os);

        return super.save(access);
    }

    @Override
    public List<AccessStatisticalVo> statisticalByDays(Integer days) {
        return accessMapper.statisticalByDays(days);
    }


}
