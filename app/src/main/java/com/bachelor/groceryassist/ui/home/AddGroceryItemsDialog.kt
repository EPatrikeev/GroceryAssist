package com.bachelor.groceryassist.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bachelor.groceryassist.R
import com.bachelor.groceryassist.model.GroceryItemModel
import kotlinx.android.synthetic.main.dialog_add_grocery_item.view.*

class AddGroceryItemsDialog(val mElement: GroceryItemModel) : DialogFragment() {
    internal lateinit var listener: AddGroceryItemsDialogListener
    private lateinit var mView: View

//    interface AddGroceryItemsDialogListener{
//        fun applyText(nameOfItem : String, quantity: Double, price : Double)
//    }

    interface AddGroceryItemsDialogListener {
        fun onDialogPositiveClick(element: GroceryItemModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        val layoutInflater = requireActivity().layoutInflater
        mView = layoutInflater.inflate(R.layout.dialog_add_grocery_item, null)

        builder.setView(mView)
//            .setTitle("Добавить продукт в список")
            .setNegativeButton("Отмена", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            .setPositiveButton("Добавить", DialogInterface.OnClickListener { dialog, id ->
                val name = mView.item_name.text.toString()
                val quantity = mView.quantity.text.toString().toDouble()
                val price = mView.price.text.toString().toDouble()
                val element= GroceryItemModel(name,quantity,price, quantity*price)

                listener.onDialogPositiveClick(element)
            })

        mView.item_name.text.insert(0, mElement.name)
        mView.quantity.text.insert(0, mElement.quantity.toString())
        mView.price.text.insert(0, mElement.price.toString())
        return builder.create()
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            listener = context as AddGroceryItemsDialogListener
//        } catch (e: ClassCastException) {
//            // The activity doesn't implement the interface, throw exception
//            throw ClassCastException(context.toString() + "must implement AddGroceryItemsDialogListener")
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = targetFragment as AddGroceryItemsDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("onAttach: ClassCastException : " + e.message)
        }
    }

    fun addElement(item: GroceryItemModel) {
        mView.item_name.text.insert(0, item.name)
        mView.quantity.text.insert(0, item.quantity.toString())
        mView.price.text.insert(0, item.price.toString())
    }
}