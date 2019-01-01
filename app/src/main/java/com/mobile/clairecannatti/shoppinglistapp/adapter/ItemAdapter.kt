package com.mobile.clairecannatti.shoppinglistapp.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobile.clairecannatti.shoppinglistapp.R
import com.mobile.clairecannatti.shoppinglistapp.R.drawable.electronics_icon
import com.mobile.clairecannatti.shoppinglistapp.ScrollingActivity
import com.mobile.clairecannatti.shoppinglistapp.data.AppDatabase
import com.mobile.clairecannatti.shoppinglistapp.data.Item
import com.mobile.clairecannatti.shoppinglistapp.touch.ItemTouchHelperAdapter
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.shopping_item.view.*
import java.util.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>,
        ItemTouchHelperAdapter {

    val context : Context
    private val shoppingList = mutableListOf<Item>()

    constructor(context: Context, shoppingList: List<Item>) : super() {
        this.context = context
        this.shoppingList.addAll(shoppingList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.shopping_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shoppingList[position]

        holder.tvItem.text = item.itemName
        holder.tvPrice.text = item.itemPrice.toString()
        holder.cbBought.isChecked = item.boughtYet

        when(item.itemCategory) {
            context.resources.getString(R.string.food) -> holder.ivIcon.setImageResource(drawable@ R.drawable.food_icon)
            context.resources.getString(R.string.electro) -> holder.ivIcon.setImageResource(drawable@electronics_icon)
            context.resources.getString(R.string.clothes) -> holder.ivIcon.setImageResource(drawable@ R.drawable.clothes_icon)
            context.resources.getString(R.string.toilet) -> holder.ivIcon.setImageResource(drawable@ R.drawable.toiletry_icon)
        }
//        holder.ivIcon.setImageResource(drawable@electronics_icon)

        holder.btnDelete.setOnClickListener{
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(
                    item, holder.adapterPosition
            )
        }

        holder.cbBought.setOnClickListener{
            item.boughtYet = holder.cbBought.isChecked

            Thread{
                AppDatabase.getInstance(context).itemDao().updateItem(item)
            }.start()
        }


    }

    private fun deleteItem(adapterPosition: Int) {
        Thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(
                    shoppingList[adapterPosition]
            )

            shoppingList.removeAt(adapterPosition)

            (context as ScrollingActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()


    }

    fun addItem(item: Item) {
        shoppingList.add(itemCount, item)
        notifyItemInserted(itemCount)
    }

    fun deleteAll() {
        for (i in 0..(itemCount-1)) {
            deleteItem(0)}
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val spCategory = itemView.spinnerType
        val ivIcon = itemView.type_icon
        val tvItem = itemView.tvItemDesc
        val cbBought = itemView.cb_bought
        val tvPrice = itemView.tvPrice
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateItem(item: Item, idx: Int) {
        shoppingList[idx] = item
        notifyItemChanged(idx)
    }


}