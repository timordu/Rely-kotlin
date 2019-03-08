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

package com.android.rely.widget.fingerprint

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.AnimationUtils
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.android.rely.widget.R
import kotlinx.android.synthetic.main.dlg_fingerprint.*

/**
 * Created by dugang on 2018/10/26.指纹验证弹窗
 */
class FingerprintAuthDialog(context: Context, onAuthListener: (success: Boolean, message: String?) -> Unit) :
        Dialog(context, R.style.Dialog_Fingerprint) {

    private val cancellationSignal: CancellationSignal by lazy { CancellationSignal() }

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dlg_fingerprint)

        action_cancel.setOnClickListener {
            dismiss()
        }
    }

    override fun show() {
        super.show()
        FingerprintManagerCompat.from(context)
                .authenticate(null, 0, cancellationSignal, fingerprintCallback, null)
    }

    override fun dismiss() {
        super.dismiss()
        cancellationSignal.cancel()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private val fingerprintCallback = object : FingerprintManagerCompat.AuthenticationCallback() {

        override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            //指纹验证成功
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            onAuthListener.invoke(true, null)
            dismiss()
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            //指纹验证失败，指纹识别失败，可再验，该指纹不是系统录入的指纹。
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
            validation_hint.text = context.getString(R.string.validation_finger_print_failed)
            validation_hint.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
        }

        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
            super.onAuthenticationError(errMsgId, errString)
            //指纹验证失败，不可再验
            onAuthListener.invoke(false, errString.toString())
            dismiss()
        }
    }
}