package com.erif.chatbubble

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erif.chatbubble.adapter.menu.AdapterMenu
import com.erif.chatbubble.adapter.menu.ItemMenu
import com.erif.chatbubble.providers.ActInstagram
import com.erif.chatbubble.providers.ActLine
import com.erif.chatbubble.providers.ActSms
import com.erif.chatbubble.providers.telegram.ActTelegram
import com.erif.chatbubble.providers.telegram.ActTelegramIos
import com.erif.chatbubble.providers.whatsapp.ActWhatsapp
import com.erif.chatbubble.providers.whatsapp.ActWhatsappIos

class MainActivity : AppCompatActivity(), AdapterMenu.OnClickListener {

    private lateinit var adapter: AdapterMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val arrIcon = arrayOf(
            R.drawable.ic_wa, R.drawable.ic_wa,
            R.drawable.ic_tele, R.drawable.ic_tele,
            R.drawable.ic_line, R.drawable.ic_ig,
            R.drawable.ic_sms
        )
        val arrName = arrayOf(
            "WhatsApp", "WhatsApp (IOS)",
            "Telegram", "Telegram (IOS)",
            "Line", "Instagram", "SMS"
        )
        val arrDest = arrayOf(
            ActWhatsapp::class.java, ActWhatsappIos::class.java,
            ActTelegram::class.java, ActTelegramIos::class.java,
            ActLine::class.java, ActInstagram::class.java,
            ActSms::class.java
        )
        val list: MutableList<ItemMenu> = ArrayList()
        arrIcon.forEachIndexed { i, icon ->
            list.add(ItemMenu(i, icon, arrName[i], arrDest[i]))
        }
        adapter = AdapterMenu(list, this)
        val recyclerView: RecyclerView = findViewById(R.id.actMainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onClickItem(item: ItemMenu) {
        val intent = Intent(this, item.destination)
        startActivity(intent)
    }
}