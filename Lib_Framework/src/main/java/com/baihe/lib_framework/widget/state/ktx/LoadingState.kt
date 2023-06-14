/*
 * Copyright (c) 2019. Dylan Cai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baihe.lib_framework.widget.state.ktx

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.dylanc.loadingstateview.LoadingStateView
import com.dylanc.loadingstateview.NavBtnType
import com.dylanc.loadingstateview.OnReloadListener
import com.dylanc.loadingstateview.ToolbarConfig

interface LoadingState {

  fun Activity.decorateContentView(listener: OnReloadListener? = null, decorative: Decorative? = null)

  fun View.decorate(listener: OnReloadListener? = null, decorative: Decorative? = null): View

  fun registerView(vararg viewDelegates: LoadingStateView.ViewDelegate)

  fun Activity.setToolbar(@StringRes titleId: Int, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null)

  fun Activity.setToolbar(title: String? = null, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null)

  fun Fragment.setToolbar(@StringRes titleId: Int, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null)

  fun Fragment.setToolbar(title: String? = null, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null)

  fun Activity.setHeaders(vararg delegates: LoadingStateView.ViewDelegate)

  fun Fragment.setHeaders(vararg delegates: LoadingStateView.ViewDelegate)

  fun Activity.setDecorView(delegate: LoadingStateView.DecorViewDelegate)

  fun Fragment.setDecorView(delegate: LoadingStateView.DecorViewDelegate)

  fun showLoadingView(animation: LoadingStateView.Animation? = null)

  fun showContentView(animation: LoadingStateView.Animation? = null)

  fun showErrorView(animation: LoadingStateView.Animation? = null)

  fun showEmptyView(animation: LoadingStateView.Animation? = null)

  fun showCustomView(viewType: Any)

  fun updateToolbar(block: ToolbarConfig.() -> Unit)

  fun <T : LoadingStateView.ViewDelegate> updateView(viewType: Any, block: T.() -> Unit)

  @Suppress("FunctionName")
  fun ToolbarViewDelegate(
    title: String? = null, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null
  ): BaseToolbarViewDelegate
}