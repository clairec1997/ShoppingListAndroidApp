package com.mobile.clairecannatti.shoppinglistapp.touch

interface ItemTouchHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}