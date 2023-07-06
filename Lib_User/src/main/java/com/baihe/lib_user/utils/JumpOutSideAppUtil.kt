package com.baihe.lib_user.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri

object JumpOutSideAppUtil {
    fun jump(activity: Activity, url: String?): Boolean {
        return try {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(url)
            intent.data = contentUrl
            activity.startActivity(intent)
            true
        } catch (var4: ActivityNotFoundException) {
            false
        }
    }
}