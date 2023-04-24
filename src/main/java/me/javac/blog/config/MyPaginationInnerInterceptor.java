package me.javac.blog.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor {

    public MyPaginationInnerInterceptor(DbType dbType) {
        super.setDbType(dbType);
    }

    @Override
    protected void handlerOverflow(IPage<?> page) {
        page.setCurrent(page.getPages());
    }
}
