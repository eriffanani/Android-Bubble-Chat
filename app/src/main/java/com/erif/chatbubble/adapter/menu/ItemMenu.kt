package com.erif.chatbubble.adapter.menu

import androidx.appcompat.app.AppCompatActivity

data class ItemMenu (
    val id: Int,
    val icon: Int,
    val name: String,
    val destination: Class<out AppCompatActivity>
)