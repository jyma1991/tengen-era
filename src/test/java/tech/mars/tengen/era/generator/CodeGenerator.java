package tech.mars.tengen.era.generator;
/**
 * @DESCRIPTION:
 * @author majunyang
 * @since 2022年9月6日 上午11:34:21
 */

import org.junit.jupiter.api.Test;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 类CodeGenerator的实现描述：TODO 类实现描述
 * @author majunyang 2022/9/6 11:34
 */
public class CodeGenerator extends BaseGenerator{

    @Test
    public void generator() {
        FastAutoGenerator.create("jdbc:postgresql://localhost:5432/postgres", "postgres", "ma")
                .globalConfig(builder -> {
                    builder.author("majunyang") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("D:\\testOut"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("tech.mars.tengen.era"); // 设置父包名
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok().entityBuilder();
                    builder.addInclude("t_user") // 设置需要生成的表名
                            .addTablePrefix("t_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}