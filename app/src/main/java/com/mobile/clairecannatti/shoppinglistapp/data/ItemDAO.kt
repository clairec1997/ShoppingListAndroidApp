package com.mobile.clairecannatti.shoppinglistapp.data

import android.arch.persistence.room.*

@Dao
interface ItemDAO {

    @Query("SELECT * FROM items")
    fun findAllItems(): List<Item>

    @Insert
    fun insertItem(item: Item) : Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)
}