package com.erif.chatbubble.providers.telegram

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ChatItemDecoration
import com.erif.chatbubble.adapter.chat.Chats
import com.erif.chatbubble.adapter.chat.telegram.android.AdapterChatTelegram

class ActTelegram : AppCompatActivity() {

    private val chats = Chats()
    private lateinit var adapter: AdapterChatTelegram

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.act_telegram)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView: RecyclerView = findViewById(R.id.act_tele_recycler)
        val list = chats.getList()
        adapter = AdapterChatTelegram(list)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(ChatItemDecoration(list))

    }
}