package com.baihe.lib_common.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.baihe.lib_common.constant.KeyConstant.KEY_PHOTOS
import com.baihe.lib_common.constant.RequestCode
import com.baihe.lib_common.databinding.ActivityPhotoListBinding
import com.baihe.lib_common.ui.adapter.PictureListAdapter
import com.baihe.lib_common.viewmodel.CommonViewModel
import com.baihe.lib_framework.base.BaseMvvmActivity
import com.baihe.lib_framework.ext.ViewExt.click
import com.baihe.lib_framework.log.LogUtil
import com.baihe.lib_framework.toast.TipsToast
import com.dylanc.loadingstateview.ViewType
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class PhotoPickActivity: BaseMvvmActivity<ActivityPhotoListBinding,CommonViewModel>() {

    private val photoListAdapter by lazy {
        PictureListAdapter().apply {
            maxSize = 9
        }
    }

    companion object {
        fun start(act:Activity){
            AndPermission.with(act)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted {
                    val intent = Intent(act, PhotoPickActivity::class.java)
                    act.startActivityForResult(intent, RequestCode.REQUEST_PHOTO)
                }
                .onDenied{
                    TipsToast.showTips("请打开读取权限")
                }.start()
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        mViewModel.loadingStateLiveData.observe(this){
            when(it){
                ViewType.LOADING ->{
                    showLoadingView()
                }
                ViewType.CONTENT -> {
                    showContentView()
                }
                ViewType.EMPTY -> {
                    showEmptyView()
                }
                ViewType.ERROR -> {
                    showErrorView()
                }
                else -> LogUtil.d(it.name)
            }
        }
        mViewModel.photoLiveData.observe(this){
            photoListAdapter.setData(it)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setToolbar{
            title = "选择照片"
        }
        mBinding.photoListRv.adapter = photoListAdapter
        mBinding.photoListRv.layoutManager = GridLayoutManager(this, 4)

    }

    override fun initListener() {
        super.initListener()
        mBinding.photoListSelectBtn.click {



            if (!photoListAdapter.selectPhotos.isEmpty()) {
                val sendPhotoPaths = ArrayList<String>()
                photoListAdapter.selectPhotos.forEach {
                    sendPhotoPaths.add(it.photoPath)
                }
                val intentData = Intent()
                intentData.putStringArrayListExtra(
                    KEY_PHOTOS,
                    sendPhotoPaths
                )
                setResult(RESULT_OK, intentData)
                finish()
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getPhotoList()
    }
}