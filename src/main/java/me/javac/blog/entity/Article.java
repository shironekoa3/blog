package me.javac.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */

@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章分类编号
     */
    private Long categoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否置顶（0否 1是）
     */
    private Byte isTop;

    /**
     * 文章状态（0正常 1草稿）
     */
    private Byte status;

    /**
     * 文章访问量
     */
    private Integer viewCount;

    /**
     * 是否允许评论（0否 1是）
     */
    private Byte isComment;

    /**
     * 创建用户
     */
    private Long createBy;

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

    /**
     * 文章分类
     */
    @TableField(exist = false)
    private Category category;

    /**
     * 文章标签列表
     */
    @TableField(exist = false)
    private List<Tag> tags;

}
