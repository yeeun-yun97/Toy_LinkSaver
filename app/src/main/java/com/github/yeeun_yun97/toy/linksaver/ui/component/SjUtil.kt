package com.github.yeeun_yun97.toy.linksaver.ui.component

class SjUtil {
    companion object{
        fun checkUrlPrefix(url:String):Boolean{
            return url.startsWith("https://") || url.startsWith("http://")
        }
    }
}