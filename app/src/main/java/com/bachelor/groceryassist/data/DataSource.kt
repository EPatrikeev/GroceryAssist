package com.bachelor.groceryassist.data

import com.bachelor.groceryassist.model.GroceryItemModel

class DataSource {

    companion object {
        fun createDataSet():ArrayList<GroceryItemModel>{
            val list = ArrayList<GroceryItemModel>()
            list.add(
                GroceryItemModel(
                    "Хлеб",
                    1.0,
                    40.0,
                    40.0
                )
            )
            list.add(
                GroceryItemModel(
                    "Молоко",
                    2.0,
                    65.90,
                    131.8
                )
            )
            list.add(
                GroceryItemModel(
                    "Бананы",
                    1.5,
                    69.00,
                    103.5
                )
            )
            return list
        }
    }
}