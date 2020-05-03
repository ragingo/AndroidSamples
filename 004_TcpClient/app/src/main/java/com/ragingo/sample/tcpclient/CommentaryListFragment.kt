package com.ragingo.sample.tcpclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_commentary_list.*
import kotlinx.coroutines.*

class CommentaryListFragment : Fragment() {

    var channelId: String = ""

    private val adapter = CommentaryListAdapter()
    private var currentJob: Job? = null
    private var commentary: Commentary? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_commentary_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.comments.clear()
        commentary_list.clearDisappearingChildren()

        currentJob =
            GlobalScope.launch(Dispatchers.Default) {
                commentary = Commentary(channelId)
                commentary!!.subscribe {
                    adapter.comments.add(0, it)
                    if (adapter.comments.size > 1000) {
                        adapter.comments.removeAt(adapter.comments.size - 1)
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
        commentary?.dispose()
        commentary = null
    }
}
