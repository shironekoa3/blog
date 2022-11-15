package me.javac.blog.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author shironekoa3
 * @since 2022-11-15
 *
 */

public class FastAutoGeneratorTest {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/blog?characterEncoding=utf8&serverTimezone=UTC", "root", "root")
                .globalConfig(builder -> builder.author("shironekoa3")
                        .outputDir("E:\\Projects\\idea\\blog\\src\\main\\java\\")
                        .disableOpenDir())
                .packageConfig(builder -> {
                    builder.parent("me.javac.blog") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\Projects\\idea\\blog\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> builder.addInclude("article") // 设置需要生成的表名
                        .addInclude("article_tag")
                        .addInclude("category")
                        .addInclude("comment")
                        .addInclude("link")
                        .addInclude("option")
                        .addInclude("tag")
                        .addInclude("user")
                        .entityBuilder()
//                            .enableLombok()
//                            .enableRemoveIsPrefix()
                        .idType(IdType.AUTO)
                        .addTableFills(Arrays.asList(
                                new Column("create_time", FieldFill.INSERT),
                                new Column("update_time", FieldFill.INSERT_UPDATE)
                        ))
                        .logicDeleteColumnName("is_deleted")
                        .logicDeletePropertyName("isDeleted")
                        .controllerBuilder()
                        .enableRestStyle()
                        .build())
//                .templateConfig(config->config.disable())
                .execute();
    }

}