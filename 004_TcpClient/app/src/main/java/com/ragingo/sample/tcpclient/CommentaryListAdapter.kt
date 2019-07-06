package com.ragingo.sample.tcpclient

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CommentaryListAdapter : RecyclerView.Adapter<CommentaryListAdapter.ViewHolder>() {
    val comments : MutableList<String> = mutableListOf()

    companion object {
        const val ITEM_HEIGHT = 100
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): ViewHolder {
        val item = TextView(viewGroup.context)
        item.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ITEM_HEIGHT)
        item.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        item.textSize = 20.0f
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {
        val item = viewHolder.itemView as TextView
        item.text = comments[index]
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}
