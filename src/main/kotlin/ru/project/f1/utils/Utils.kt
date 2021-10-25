package ru.project.f1.utils

import java.time.Duration
import java.time.LocalDateTime
import java.util.function.Function

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

        fun <T, R> ifNotNull(value: T?, func: Function<T, R>): R? = if (value != null) func.apply(value) else null

        fun <T> ifNotNull(value: T?, def: T): T = value ?: def

        fun <T, R> ifNotNull(value: T?, get: Function<T, R>, def: R): R =
            if (value != null) ifNotNull(get.apply(value), def) else def

        fun isBlank(value: String?): Boolean = value == null || value.trim { it <= ' ' }.isEmpty()

        fun <T> ifNotEmpty(value: T, def: T): T {
            val str = if (value is String) value else value.toString()
            return if (str.isNotEmpty()) value else def
        }

        fun <T, R> ifNotEmpty(str: T?, get: Function<T, R>): R? =
            if (str != null && str.toString().isNotEmpty()) get.apply(str) else null

        fun <T, R> ifNotEmpty(value: T, get: Function<T, R>, def: R): R {
            val str = if (value is String) value else value.toString()
            return if (str.isNotEmpty()) ifNotEmpty(get.apply(value), def) else def
        }
    }
}