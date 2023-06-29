package com.baihe.lib_common.ui.widget.state

import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.baihe.lib_common.R
import com.dylanc.loadingstateview.LoadingStateView
import com.dylanc.loadingstateview.ViewType

class LoadingViewDelegate:LoadingStateView.ViewDelegate(ViewType.LOADING) {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View =
        inflater.inflate(R.layout.common_loading_view, parent, false)
            .also {
            val ivLoading: ImageView = it.findViewById(R.id.loading_iv)
            (ivLoading.drawable as AnimationDrawable).start()
        }

}