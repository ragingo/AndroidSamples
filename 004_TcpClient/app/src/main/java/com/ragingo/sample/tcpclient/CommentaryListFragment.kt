package com.ragingo.sample.tcpclient

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_commentary_list.*
import kotlinx.coroutines.*

class CommentaryListFragment : Fragment() {

    private val adapter = CommentaryListAdapter()
    private var currentJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commentary_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.comments.clear()
        commentary_list.clearDisappearingChildren()

        currentJob =
            GlobalScope.launch(Dispatchers.Main) {
                while (true) {
                    delay(1000)
                    for (i in 1..10) {
                        adapter.comments.add(0, adapter.comments.size.toString())
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        commentary_list.layoutManager = LinearLayoutManager(context)
        commentary_list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentJob?.cancel()
    }
}
