package ru.project.f1.utils

import java.time.Duration
import java.time.LocalDateTime

class Utils {
    companion object {
        fun format(localDateTime: LocalDateTime): String {
            val now = LocalDateTime.now()
            var between = Duration.between(localDateTime, now)
            if (localDateTime.plusHours(24) > now) {
                if (localDateTime.plusMinutes(60) > now) {
                    if (localDateTime.plusSeconds(60) > now) {
                        return "${between.toSeconds()} seconds ago"
                    }
                    return "${between.toMinutes()} minutes ago"
                }
                return "${between.toHours()} hours ago"
            }
            return "${between.toDays()} days ago"
        }
    }
}