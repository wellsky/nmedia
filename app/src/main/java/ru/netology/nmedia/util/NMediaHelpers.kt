package ru.netology.nmedia.util

object NMediaHelpers {
    fun optimalCount(count: Int): String {
        return when {
            (count >= 1000) -> {
                if ((count % 1000 < 100) || (count >= 100000)) {
                    val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
                    String.format(
                        "%d %c",
                        (count / Math.pow(1000.0, exp.toDouble())).toInt(),
                        "kMGTPE"[exp - 1]
                    )
                } else {
                    val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
                    String.format(
                        "%.1f %c",
                        count / Math.pow(1000.0, exp.toDouble()),
                        "kMGTPE"[exp - 1]
                    )
                }
            }
            else -> count.toString()
        }
    }
}