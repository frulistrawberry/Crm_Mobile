package com.baihe.imageloader.ext

import android.widget.ImageView
import com.baihe.imageloader.ImageLoaderUtils

fun ImageView.loadImageUrl(url:String?){
    ImageLoaderUtils.getInstance().loadImage(context,this,url)
}

fun ImageView.loadImageUrl(url:String?,errorRes:Int,placeRes:Int){
    ImageLoaderUtils.getInstance().loadImage(context,this,url,errorRes,placeRes)
}

fun ImageView.loadImageRes(drawableRes:Int){
    ImageLoaderUtils.getInstance().displayFromDrawable(context,drawableRes,this)
}

fun ImageView.loadImageFile(url: String?){
    ImageLoaderUtils.getInstance().displayFromSD(context,url,this)
}

fun ImageView.loadCircleImage(url:String){
    ImageLoaderUtils.getInstance().loadCircleImage(context,this,url)
}

fun ImageView.loadRoundImage(url:String,radius:Int){
    ImageLoaderUtils.getInstance().loadCornerImage(context,this,url,radius)
}

fun ImageView.loadGif(url:String){
    ImageLoaderUtils.getInstance().loadGifImage(context,this,url)
}
