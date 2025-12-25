package com.skul9x.dosecalculator

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DrugPersistence {

    private const val PREFS_NAME = "DrugPrefs"
    private const val DRUGS_KEY = "drugs_list"
    private val gson = Gson()

    // Hàm cung cấp danh sách thuốc mặc định, giống như trong code Python
    private fun getDefaultDrugs(): MutableList<Drug> {
        return mutableListOf(
            Drug("ZT-Amox", 200, 5, 50),
            Drug("Cefdinir", 125, 5, 14),
            Drug("Bactirid", 100, 5, 8),
            Drug("Cefprozil (liều 20)", 250, 5, 20),
            Drug("Cefprozil (liều 30)", 250, 5, 30),
            Drug("Biseptol", 240, 5, 48),
            Drug("ZiUSA", 200, 5, 10)
        )
    }

    // Hàm lưu danh sách thuốc
    fun saveDrugs(context: Context, drugs: List<Drug>) {
        val sortedDrugs = drugs.sortedBy { it.name.lowercase() } // Luôn sắp xếp trước khi lưu
        val jsonString = gson.toJson(sortedDrugs)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(DRUGS_KEY, jsonString)
            .apply()
    }

    // Hàm tải danh sách thuốc
    fun loadDrugs(context: Context): MutableList<Drug> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(DRUGS_KEY, null)

        return if (jsonString != null) {
            val type = object : TypeToken<MutableList<Drug>>() {}.type
            gson.fromJson(jsonString, type)
        } else {
            // Nếu chưa có gì được lưu, trả về danh sách mặc định và lưu lại
            val defaultDrugs = getDefaultDrugs()
            saveDrugs(context, defaultDrugs) // Lưu lại phiên bản đã sắp xếp
            defaultDrugs
        }
    }
}