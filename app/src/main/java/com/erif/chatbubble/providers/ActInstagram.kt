package com.erif.chatbubble.providers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.CountDown.CountDown
import com.erif.bubble.instagram.BubbleInstagram
import com.erif.chatbubble.R
import com.erif.chatbubble.adapter.chat.ChatItemDecoration
import com.erif.chatbubble.adapter.chat.Chats
import com.erif.chatbubble.adapter.chat.instagram.AdapterChatInstagram
import com.google.android.material.appbar.MaterialToolbar

class ActInstagram : AppCompatActivity() {

    private val chats = Chats()
    private lateinit var adapter: AdapterChatInstagram

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.act_instagram)
        //val toolbar: MaterialToolbar = findViewById(R.id.act_ig_toolbar)
        //setSupportActionBar(toolbar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val list = chats.getList()
        adapter = AdapterChatInstagram(list)
        val recycler: RecyclerView = findViewById(R.id.act_ig_recycler)
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        recycler.layoutManager = manager
        recycler.adapter = adapter
        recycler.addItemDecoration(ChatItemDecoration(list))

    }
}