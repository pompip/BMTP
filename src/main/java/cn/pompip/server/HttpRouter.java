/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRouter {
    /**
     * 路由的路径
     * @return
     */
    String uri() default "/";
}
