package com.skul9x.dosecalculator

import java.io.Serializable

// Dùng Serializable để có thể truyền đối tượng này giữa các Activity
data class Drug(
    val name: String,
    val mg: Int,
    val ml: Int,
    val dose: Int
) : Serializable