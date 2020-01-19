package com.wei.rece.net.conf.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本地是否创建备份目录
 * @author 		weih
 * @date   		2013-7-4上午10:18:54
 * @version     
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface BackUpDirComment {

}
