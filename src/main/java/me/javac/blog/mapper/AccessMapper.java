package me.javac.blog.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.javac.blog.entity.Access;
import me.javac.blog.vo.AccessStatisticalVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
public interface AccessMapper extends BaseMapper<Access> {

    List<AccessStatisticalVo> statisticalByDays(Integer days);

}
