package com.astashin.bookvoed.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "avatar")
class AvatarStorageProperties {
    var uploadDir: String? = null
}