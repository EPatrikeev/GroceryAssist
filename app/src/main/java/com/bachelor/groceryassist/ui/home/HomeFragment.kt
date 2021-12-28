package com.bachelor.groceryassist.ui.home

import SwipeToDeleteCallback
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bachelor.groceryassist.R
import com.bachelor.groceryassist.model.GroceryItemAdapter
import com.bachelor.groceryassist.model.GroceryItemModel
import com.bachelor.groceryassist.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment(), AddGroceryItemsDialog.AddGroceryItemsDialogListener {

    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var mGroceryAdapter: GroceryItemAdapter
    internal var mRoot: View? = null
    private var lastCountOfElements = 0
    private var dialogAfterUndo = false
    private var dialogOpened = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mHomeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mRoot = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root!!.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val fab: FloatingActionButton = mRoot!!.findViewById(R.id.fab)
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            dispatchTakePictureIntent()
        }

        initRecyclerView()
        enableSwipeToDeleteAndUndo()
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHomeViewModel.getPurchasesList().observe(viewLifecycleOwner, Observer { it ->
            it?.let { it ->
                if ((it.size > lastCountOfElements) and (!dialogAfterUndo) and (!dialogOpened)) {
                    openDialog(it[it.lastIndex])
                    dialogAfterUndo = false

                }
                lastCountOfElements = it.size
                mGroceryAdapter.submitList(it)
                mGroceryAdapter.notifyDataSetChanged()
                dialogOpened = false
                var sum = 0.0
                it.forEach {
                    sum += it.sum
                }
                mRoot!!.sum2.text = sum.toString()
//
            }
        })
    }

    // opens camera
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check for REQUEST_IMAGE_CAPTURE
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras!!["data"] as Bitmap

//            if error -> snackbar
            mHomeViewModel.detectImage(imageBitmap)
        }
    }

    private fun initRecyclerView() {
        mRoot!!.recycle_view.layoutManager = LinearLayoutManager(activity)
        mGroceryAdapter = GroceryItemAdapter()
        mRoot!!.recycle_view.adapter = mGroceryAdapter
    }

    private fun openDialog(element: GroceryItemModel) {
        dialogOpened = true
        val addGroceryDialog = AddGroceryItemsDialog(element)
        addGroceryDialog.setTargetFragment(this, 1)
        fragmentManager?.let { addGroceryDialog.show(it, "dialog") }
    }

    override fun onDialogPositiveClick(element: GroceryItemModel) {
        mHomeViewModel.replaceElement(element, mHomeViewModel.getPurchasesList().value!!.size - 1)
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

                val position = viewHolder.adapterPosition
                val item = mGroceryAdapter.getData()[position]

                mGroceryAdapter.removeItem(position)
                mHomeViewModel.deleteElement(position)

                val snack = Snackbar.make(
                    mRoot!!.findViewById(R.id.homeConstraintLayout),
                    "Элемент был удалён", Snackbar.LENGTH_LONG
                )
                snack.setAction("Отмена") {
                    dialogAfterUndo = true
                    mGroceryAdapter.restoreItem(item, position)
                    mHomeViewModel.restoreElement(item, position)
                    mRoot!!.recycle_view.scrollToPosition(position)
                }
                snack.setActionTextColor(Color.YELLOW)
                snack.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(mRoot!!.recycle_view)
    }
}