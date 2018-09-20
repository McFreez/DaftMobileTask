package com.daftmobile.daftmobiletask

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class ItemsAdapter(private val items: ArrayList<Item>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){

    var randomElementsCounter: Int = 0

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items, position, this)
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        recyclerView = rv
    }

    fun addRandomElement(){
        val color =
                if(Random().nextInt(2) == 0)
                    Color.RED
                else
                    Color.BLUE

        items.add(Item(color, Random().nextInt(10000)))
        randomElementsCounter ++
        notifyItemInserted(itemCount - 1)
    }

    fun removeRandomElement(){
        val positionToRemove = Random().nextInt(itemCount)
        items.removeAt(positionToRemove)
        randomElementsCounter --
        notifyDataSetChanged()
    }

    fun resetRandomElementCounter(){
        randomElementsCounter = 0
    }

    fun addModelValue(){
        recyclerView?.findViewHolderForAdapterPosition(itemCount - 1)?.itemView?.performClick()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        val number = view.item_number
        val colorBlue = view.item_color_blue
        val colorRed = view.item_color_red

        fun bind(items: ArrayList<Item>, position: Int, adapter: ItemsAdapter){
            if(items[position].mColor == Color.RED){
                number.text = items[position].mNumber.times(3).toString()
                colorBlue.visibility = View.GONE
                colorRed.visibility = View.VISIBLE

            } else {
                number.text = items[position].mNumber.toString()
                colorBlue.visibility = View.VISIBLE
                colorRed.visibility = View.GONE
            }

            view.setOnClickListener {
                items[position].mNumber += position
                adapter.notifyItemChanged(position)
            }
        }
    }
}