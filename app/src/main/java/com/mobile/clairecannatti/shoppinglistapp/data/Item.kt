package com.mobile.clairecannatti.shoppinglistapp.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class Item (
        @PrimaryKey(autoGenerate = true) var itemId: Long?,
        @ColumnInfo(name = "itemCategory") var itemCategory: String,
        @ColumnInfo(name = "itemName") var itemName: String,
        @ColumnInfo(name = "itemDesc") var itemDesc: String,
        @ColumnInfo(name = "boughtYet") var boughtYet: Boolean,
        @ColumnInfo(name = "itemPrice") var itemPrice: Float
) : Serializable