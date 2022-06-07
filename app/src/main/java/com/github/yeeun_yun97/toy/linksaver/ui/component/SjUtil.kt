package com.github.yeeun_yun97.toy.linksaver.ui.component

class SjUtil {
    companion object{
        fun checkUrlPrefix(url:String):Boolean{
            return url.startsWith("https://")
                    || url.startsWith("http://")
        }

        fun checkYoutubePrefix(url: String): Boolean {
            return url.startsWith("https://www.youtube.com/watch?v=")
                    || url.startsWith("http://www.youtube.com/watch?v=")
                    || url.startsWith("http://m.youtube.com/watch?v=")
                    || url.startsWith("https://m.youtube.com/watch?v=")
                    || url.startsWith("https://youtu.be/")
                    || url.startsWith("http://youtu.be/")
        }
    }
}