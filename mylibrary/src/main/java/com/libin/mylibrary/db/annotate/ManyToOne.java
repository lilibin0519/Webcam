/**
 * Copyright (c) 2012-2013, Michael Yang 鏉ㄧ娴� (www.yangfuhai.com).
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,寮犳稕.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.libin.mylibrary.db.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鏍囪瘑澶氬涓�鐨勫睘鎬у垪<br>
 * 
 * <b>鍒涘缓鏃堕棿</b> 2014-8-15
 *
 * @author kymjs (http://www.kymjs.com)
 * @author 鏉ㄧ娴� (http://www.yangfuhai.com)
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManyToOne {
    public String column() default "";
}
