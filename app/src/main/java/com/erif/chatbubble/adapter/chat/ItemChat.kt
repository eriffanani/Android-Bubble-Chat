package com.erif.chatbubble.adapter.chat

import com.erif.bubble.Bubbles

data class ItemChat (
    val id: Int,
    val message: String?,
    val type: Int,
    val condition: Bubbles.BubbleCondition
)