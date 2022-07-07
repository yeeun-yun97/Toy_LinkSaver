package com.github.yeeun_yun97.toy.linksaver.data.model

import androidx.room.TypeConverter

enum class ELinkType() { video, link; }

class Converters {
    @TypeConverter
    fun toELinkType(value: String) = enumValueOf<ELinkType>(value)
    @TypeConverter
    fun fromELinkType(value: ELinkType) = value.name
}

