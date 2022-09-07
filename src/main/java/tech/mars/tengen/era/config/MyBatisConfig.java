package tech.mars.tengen.era.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-plus configuration, add pagination interceptor.
 *
 * @author majunyang
 */
@Configuration
@EnableTransactionManagement
@MapperScan("tech.mars.tengen.era.mapper")
public class MyBatisConfig {
    /**
     * add pagination interceptor.
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

//    /**
//     * 定义MP全局策略
//     *
//     * @return
//     */
//    @Bean
//    public GlobalConfig getGlobalConfig() {
//        GlobalConfig conf = new GlobalConfig();
//        conf.setSqlInjector(new DefaultSqlInjector());
//        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
//        //主键ID策略
//        dbConfig.setIdType(IdType.NONE);
//        // 逻辑删除全局值
//        dbConfig.setLogicDeleteValue(Constants.Y.toUpperCase());
//        // 逻辑未删除全局值
//        dbConfig.setLogicNotDeleteValue(Constants.N.toUpperCase());
//        //表名和字段名是否使用下划线命名
//        dbConfig.setTableUnderline(true);
//        conf.setDbConfig(dbConfig);
//        return conf;
//    }
}
