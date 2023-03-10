package me.javac.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论类型（0文章 1友链）
     */
    private Byte type;

    /**
     * 评论文章编号
     */
    private Long articleId;

    /**
     * 评论父编号
     */
    private Long rootId;

    /**
     * 评论者头像URL
     */
    private String avatar;

    /**
     * 评论者用户名
     */
    private String nick;

    /**
     * 评论者邮箱
     */
    private String email;

    /**
     * 评论者网站
     */
    private String website;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Byte isDeleted;


}
