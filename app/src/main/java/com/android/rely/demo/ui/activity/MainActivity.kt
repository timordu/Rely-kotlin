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

package com.android.rely.demo.ui.activity

import com.dugang.rely.condition_skip.ConditionSkip
import com.dugang.rely.condition_skip.Valid
import com.android.rely.demo.Contains
import com.android.rely.demo.R
import com.android.rely.demo.ui.activity.conditionskip.Login2Activity
import com.android.rely.demo.ui.parent.MyBaseActivity
import com.android.rely.demo.util.valid.LoginValid
import com.dugang.rely.common.extension.skipToActivity
import com.dugang.rely.common.extension.initToolBar
import com.android.rely.demo.ui.activity.conditionskip.ResultActivity
import com.android.rely.demo.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.act_main.*

class MainActivity : MyBaseActivity() {
    override val layoutResId: Int = R.layout.act_main
    private val viewModel: MainViewModel by lazy { getViewModel<MainViewModel>() }


    override fun initView() {
        initToolBar("功能测试")

        widget.setOnClickListener {
            skipToActivity<WidgetActivity>()
        }

        media.setOnClickListener {
            skipToActivity<MediaActivity>()
        }

        condition_skip.setOnClickListener {
            ConditionSkip.add("skipToConditionSkipActivity")
                .addValid(LoginValid(mContext))
                .addValid(object : Valid(mContext, 6) {
                    override fun check(): Boolean = Contains.isLogin2

                    override fun doValid() = skipToActivity<Login2Activity>()

                })
                .validComplete {
                    skipToActivity<ResultActivity>()
                }
                .doCall()
        }

        network_test.setOnClickListener {
            viewModel.testNetwork("123", "4345")
        }
    }


    override fun initObserve() {}


}
