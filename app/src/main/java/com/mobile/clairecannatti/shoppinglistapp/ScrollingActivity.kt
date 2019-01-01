package com.mobile.clairecannatti.shoppinglistapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchUIUtil
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mobile.clairecannatti.shoppinglistapp.adapter.ItemAdapter
import com.mobile.clairecannatti.shoppinglistapp.data.AppDatabase
import com.mobile.clairecannatti.shoppinglistapp.data.Item
import com.mobile.clairecannatti.shoppinglistapp.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    private lateinit var itemAdapter : ItemAdapter
    private var editIndex : Int = 0

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        btnAddItem.setOnClickListener{view ->
            showAddItemDialog()
        }

        initRecyclerView()

        btnDeleteAll.setOnClickListener {
            itemAdapter.deleteAll()
        }

    }

    private fun initRecyclerView() {
        Thread {
            val shoppingList = AppDatabase.getInstance(
                    this@ScrollingActivity).itemDao().findAllItems()
            runOnUiThread {
                itemAdapter = ItemAdapter(this@ScrollingActivity, shoppingList)
                val callback = ItemTouchHelperCallback(itemAdapter)
                val touchHelper = ItemTouchHelper(callback)

                shoppingRecycler.adapter = itemAdapter

                touchHelper.attachToRecyclerView(shoppingRecycler)
            }
        }.start()
    }

    private fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, "TAG_CREATE")
    }

    public fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx
        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
                "EDITITEMDIALOG")
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_scrolling, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(menuitem: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        return when (menuitem.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun itemCreated(item: Item) {
        Thread {
            val id = AppDatabase.getInstance(
                    this@ScrollingActivity).itemDao().insertItem(item)
            item.itemId = id

            runOnUiThread {
                itemAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        val dbThread = Thread {
            AppDatabase.getInstance(this@ScrollingActivity).itemDao().updateItem(item)

            runOnUiThread { itemAdapter.updateItem(item, editIndex) }
        }
        dbThread.start()
    }

}
