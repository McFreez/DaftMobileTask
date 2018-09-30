package com.daftmobile.daftmobiletask

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class ItemsAdapter(private val items: ArrayList<Item>, private val context: Context) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items, position, this, context)

    private fun randomizePosition() = Random().nextInt(itemCount)

    fun addRandomElement() =
        with(if(Random().nextBoolean())
                    Color.RED
                else
                    Color.BLUE){
            items.add(Item(this, Random().nextInt(100)))
            notifyItemInserted(itemCount - 1)
        }

    fun incrementRandomElement() = with(randomizePosition()){
            items[this].number ++
            notifyItemChanged(this)
        }

    fun removeRandomElement() = with(randomizePosition()){
            items.removeAt(this)
            notifyItemRemoved(this)
        }

    fun resetRandomElementCounter() = with(randomizePosition()){
            items[this].number = 0
            notifyItemChanged(this)
        }

    fun addModelValue(position : Int = randomizePosition()){
        items[position].number +=
                if(position == 0)
                    0
                else
                    // or items[position - 1].number.absoluteValue if absolute value needed
                    items[position - 1].number

        notifyItemChanged(position)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        val number = view.item_number
        val color = view.item_color

        fun bind(items: ArrayList<Item>, position: Int, adapter: ItemsAdapter, context: Context){
            if(items[position].color == Color.RED){
                number.text = items[position].number.times(3).toString()
                color.setImageDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorItemRed)))
            } else {
                number.text = items[position].number.toString()
                color.setImageDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.colorItemBlue)))
            }
            color.tag = items[position].color

            view.setOnClickListener { adapter.addModelValue(position) }
        }
    }
}