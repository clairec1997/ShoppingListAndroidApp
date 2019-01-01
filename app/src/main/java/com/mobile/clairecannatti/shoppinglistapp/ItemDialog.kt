package com.mobile.clairecannatti.shoppinglistapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.mobile.clairecannatti.shoppinglistapp.data.Item
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.shopping_item.view.*

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException("The activity does not implement the ItemHandler interface")
        }
    }

    //private val categories = listOf(getString(R.string.food), getString(R.string.toilet), getString(R.string.clothes), getString(R.string.electro))
    //extracting the strings caused the app to quit when making a new item, so this is hard-coded
    //by necessity
    private val categories = listOf("Food", "Toiletries", "Clothes", "Electronics")

    lateinit var cat: String
    private lateinit var etItemName : EditText
    private lateinit var etItemDesc : EditText
    private lateinit var etItemPrice : EditText
    private lateinit var spItemCategory : Spinner
    private var itemIcon : ImageView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("New Reminder")

        val rootView = requireActivity().layoutInflater.inflate(R.layout.item_dialog, null)

        etItemName = rootView.etItemName
        etItemDesc = rootView.etItemDesc
        etItemPrice = rootView.etPrice
        spItemCategory = rootView.spinnerType
        itemIcon = rootView.type_icon

        spItemCategory.adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, categories)

        spItemCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                cat = parent.getItemAtPosition(position).toString()
            }

        }

        builder.setView(rootView)

        val arguments = this.arguments

        if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(
                    ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Item

            etItemName.setText(item.itemName)
            etItemPrice.setText(item.itemPrice.toString())
            etItemDesc.setText(item.itemDesc)

            when(item.itemCategory) {
                getString(R.string.food) -> spItemCategory.setSelection(0)
                getString(R.string.toilet) -> spItemCategory.setSelection(1)
                getString(R.string.clothes) -> spItemCategory.setSelection(2)
                getString(R.string.electro) -> spItemCategory.setSelection(3)
            }

            builder.setTitle(getString(R.string.edit_item))
        }

        builder.setPositiveButton(getString(R.string.ok)){
            dialog, witch -> //empty
        }

        builder.setNegativeButton(getString(R.string.nvm)){
            dialog, witch ->
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener{

            if (etItemName.text.isNotEmpty() && etItemPrice.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else if (etItemPrice.text.isNotEmpty()) {
                etItemName.error = getString(R.string.empty_error)
            } else {
                etItemPrice.error = getString(R.string.empty_error)
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
                Item(
                        null,
                        cat,
                        etItemName.text.toString(),
                        etItemDesc.text.toString(),
                        false,
                        etItemPrice.text.toString().toFloat()
                )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Item

        itemToEdit.itemName = etItemName.text.toString()
        itemToEdit.itemPrice = etItemPrice.text.toString().toFloat()
        itemToEdit.itemCategory = cat
        itemToEdit.itemDesc = etItemDesc.text.toString()

        itemHandler.itemUpdated(itemToEdit)
    }
}