package com.example.architecture.home.ui.allgames

import com.example.architecture.home.ui.model.BaseModel
import com.example.architecture.home.ui.model.allgames.*


// -------- for test
fun fetchGamesPrimaryItems(): MutableList<PrimaryContentModel> {
    return mutableListOf<PrimaryContentModel>().apply {
        secondaryMultiItems().forEachIndexed { index, item ->
            add(PrimaryContentModel(content = item.content, index == 0))
        }
    }
}

fun fetchLotteryPrimaryItems(): MutableList<PrimaryContentModel> {
    return mutableListOf<PrimaryContentModel>().apply {
        (0..12).forEachIndexed { index, item ->
            add(PrimaryContentModel(content = "测试", index == 0))
        }
    }
}


fun secondaryMultiItems(): List<BaseModel> {
    return listOf(
        Model("测试"),
        DoubleItemModel("测试"),
        ThreeItemModel("测试"),
        FourItemModel("测试"),
        FiveItemModel("测试"),
        SixItemModel("测试")
    )
}

fun secondaryItems(): List<String> {
    return listOf(
        "1","1","1","1","2","1","1","1","1","1","2","1","1","1","1","1","2","1","1","1","1",
    )
}