/*
 *    Copyright (c) 2017-2019 dugang
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.android.rely

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.ALog

@SuppressLint("StaticFieldLeak")
object Rely {
    lateinit var appContext: Context
    var isDebug: Boolean = true

    /**
     * 初始化工具类
     */
    fun init(context: Context, debug: Boolean) {
        appContext = context.applicationContext
        isDebug = debug
        //初始化日志工具
        ALog.init(context).setLogSwitch(debug)
    }
}