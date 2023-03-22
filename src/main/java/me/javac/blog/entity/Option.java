package me.javac.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@Data
@TableName(value = "`option`")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置项
     */
    @TableField(value = "`key`")
    private String key;

    /**
     * 配置值
     */
    @TableField(value = "`value`")
    private String value;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;


}
