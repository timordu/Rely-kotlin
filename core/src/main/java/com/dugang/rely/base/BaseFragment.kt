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

package com.dugang.rely.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dugang.rely.Rely
import com.dugang.rely.common.extension.showToast
import com.dugang.rely.eventbus.MsgEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by dugang on 2017/7/26.Fragment基类
 */
@Suppress("unused")
abstract class BaseFragment : Fragment(), LifecycleOwner {
    protected open val mContext get() = activity as Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutResId, container, false)

    protected abstract val layoutResId: Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        initObserve()
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    abstract fun initObserve()

    open fun showLoadingDialog() {}

    open fun dismissLoadingDialog() {}

    protected fun <VM : BaseViewModel> initViewModel(clazz: Class<VM>): VM {
        return ViewModelProviders.of(this).get(clazz).apply {
            isShowLoading.observe(this@BaseFragment, Observer {
                if (it) showLoadingDialog() else dismissLoadingDialog()
            })
            lifecycle.addObserver(this)
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }


    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MsgEvent) {
        if (event.code == Rely.NET_CODE_ERROR) showToast("${event.msg}")
    }
}


