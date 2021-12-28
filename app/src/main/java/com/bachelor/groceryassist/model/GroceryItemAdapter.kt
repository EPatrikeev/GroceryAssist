package com.bachelor.groceryassist.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.groceryassist.R
import kotlinx.android.synthetic.main.grocery_item.view.*

class GroceryItemAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mGroceryList: ArrayList<GroceryItemModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroceryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grocery_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mGroceryList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GroceryItemViewHolder -> {
                holder.bind(mGroceryList[position])
            }
        }
    }

    fun submitList(itemList: ArrayList<GroceryItemModel>) {
        mGroceryList = itemList

    }

    fun addElement(item: GroceryItemModel) {
        mGroceryList.add(item)
        notifyItemInserted(mGroceryList.size)
    }

    fun removeAt(position: Int) {
        mGroceryList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getData(): ArrayList<GroceryItemModel> {
        return mGroceryList
    }

    fun removeItem(position: Int) {
        val arrayList = ArrayList<GroceryItemModel>(mGroceryList)
        arrayList.removeAt(position)
        mGroceryList = arrayList
        notifyItemRemoved(position)
    }

    fun restoreItem(item: GroceryItemModel, position: Int) {
        val arrayList = ArrayList<GroceryItemModel>(mGroceryList)
        arrayList.add(position, item)
        mGroceryList = arrayList
        notifyItemInserted(position)
    }

    class GroceryItemViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
//        val titleItem = itemView.title_grocery_item
//        val quantityItem = itemView.quantity_grocery_item
//        val priceItem = itemView.price_grocery_item
//        val sumItem = itemView.sum_grocery_item

        fun bind(groceryItemModel: GroceryItemModel) {
            itemView.title_grocery_item.text = groceryItemModel.name
            itemView.quantity_grocery_item.text = groceryItemModel.quantity.toString()
            itemView.price_grocery_item.text = groceryItemModel.price.toString()
            itemView.sum_grocery_item.text = groceryItemModel.sum.toString()
        }
    }
}