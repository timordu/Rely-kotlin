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

import android.content.Intent
import androidx.lifecycle.Observer
import com.android.rely.common.listview.SimpleAdapter
import com.android.rely.common.setOnSeekBarChangeListener
import com.android.rely.common.showToast
import com.android.rely.demo.R
import com.android.rely.demo.ui.activity.widget.FingerprintActivity
import com.android.rely.demo.ui.activity.widget.SideIndexBarDemoActivity
import com.android.rely.demo.ui.parent.MyBaseActivity
import com.android.rely.demo.ui.viewmodel.WidgetViewModel
import com.android.rely.ext.loadImage
import com.android.rely.common.skipToActivity
import com.android.rely.common.smoothSwitchScreen
import com.android.rely.common.initToolBar
import com.android.rely.widget.image.ImagePreview
import com.android.rely.widget.datetime.DateTimePicker
import kotlinx.android.synthetic.main.act_widget.*
import kotlinx.android.synthetic.main.item_widget.view.*


class WidgetActivity : MyBaseActivity() {
    override val layoutResId: Int = R.layout.act_widget

    private val viewModel: WidgetViewModel by lazy { getViewModel<WidgetViewModel>() }

    override fun initView() {
        smoothSwitchScreen()
        initToolBar("自定义组件测试", R.mipmap.icon_back)

        fingerprint.setOnClickListener {
            skipToActivity<FingerprintActivity>()
        }

        seekBar.setOnSeekBarChangeListener {
            onProgressChanged { _, progress, _ ->
                numberProgressBar.setProgress(progress)
            }
        }


        date.setOnClickListener {
            DateTimePicker.selectDate(mContext) {
                showToast(it)
            }
        }

        time.setOnClickListener {
            DateTimePicker.selectTime(mContext) {
                showToast(it)
            }
        }

        datetime.setOnClickListener {
            DateTimePicker.selectDateTime(mContext) {
                showToast(it)
            }
        }


        sideIndexBar.setOnClickListener {
            skipToActivity<SideIndexBarDemoActivity>()
        }
    }

    override fun initObserve() {
        viewModel.singleImage.observe(this, Observer { url ->
            single_image.loadImage(url)
            single_image.setOnClickListener {
                ImagePreview.show(this, single_image, arrayListOf(url))
            }
        })
        viewModel.multiImage.observe(this, Observer { urlList ->
            val adapter = SimpleAdapter(this, R.layout.item_widget, urlList) { view, data ->
                view.imageView.loadImage(data)
            }
            multi_image.adapter = adapter
            multi_image.setOnItemClickListener { _, _, position, _ ->
                ImagePreview.show(this, multi_image.getChildAt(position).imageView, urlList, position)
            }
        })
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        ImagePreview.onActivityReenter(this, data, multi_image)
    }


}