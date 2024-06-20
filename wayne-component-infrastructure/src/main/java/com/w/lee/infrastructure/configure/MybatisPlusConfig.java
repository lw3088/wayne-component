package com.w.lee.infrastructure.configure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;


/**
 * MybatisPlus 配置
 *
 * @author w.lee
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class MybatisPlusConfig {

    /**
     * 自动填充处理器
     */
    @Bean
    public AutoFillMetaObjectHandler autoFillMetaObjectHandler() {
        return new AutoFillMetaObjectHandler();
    }


    /**
     * 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        //乐观锁拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 取消使用动态表名
        //mybatisPlusInterceptor.addInnerInterceptor(new CustomizeDynamicTableNameInnerInterceptor());

        //阻断拦截器   攻击 SQL 阻断解析器,防止全表更新与删除
        mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return mybatisPlusInterceptor;
    }


    /**
     * 新增或修改自动填充
     */
    public static class AutoFillMetaObjectHandler implements MetaObjectHandler {

        private static final String UPDATE_TIME = "updateTime";
        private static final String UPDATER_BY = "updateBy";

        private static final String CREATE_TIME = "createTime";
        private static final String CREATE_BY = "createBy";


        /**
         * 插入填充，字段为空自动填充
         */
        @Override
        public void insertFill(MetaObject metaObject) {

            Object createTime = getFieldValByName(CREATE_TIME, metaObject);
            Object createId = getFieldValByName(CREATE_BY, metaObject);
            Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
            Object updateId = getFieldValByName(UPDATER_BY, metaObject);
            Long userId = getUserId();
            if (null == createTime) {
                //填充创建时间
                setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
            }
            if (null == createId && null != userId) {
                //填充创建者id
                setFieldValByName(CREATE_BY, userId.intValue(), metaObject);
            }
            if (null == updateTime) {
                //填充修改时间
                setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
            }
            if (null == updateId && null != userId) {
                //填充修改人id
                setFieldValByName(UPDATER_BY, userId.intValue(), metaObject);
            }
        }

        /**
         * 更新填充
         */
        @Override
        public void updateFill(MetaObject metaObject) {
            Object updateTime = getFieldValByName(UPDATE_TIME, metaObject);
            Object updateId = getFieldValByName(UPDATER_BY, metaObject);
            Long userId = getUserId();
            if (updateTime == null) {
                //填充修改时间
                setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
            }
            if (null == updateId && null != userId) {
                // //填充修改人id
                setFieldValByName(UPDATER_BY, userId.intValue(), metaObject);
            }

        }

        private Long getUserId() {
            try {
                return null ;
            } catch (Exception ignored) {
                return null;
            }
        }

    }


}
