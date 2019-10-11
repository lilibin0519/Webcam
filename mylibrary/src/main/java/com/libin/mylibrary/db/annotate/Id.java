package com.libin.mylibrary.db.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id��������,�����õ�ʱ��Ĭ�������id��_id�ֶ���Ϊ������column�����õ���Ĭ��Ϊ�ֶ��� <br>
 * 
 * <b>����ʱ��</b> 2014-8-15
 *
 * @author kymjs (http://www.kymjs.com)
 * @author ��� (http://www.yangfuhai.com)
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    /**
     * ����Ϊ����
     * 
     * @return
     */
    public String column() default "";
}
