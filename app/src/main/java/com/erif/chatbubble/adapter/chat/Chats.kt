package com.erif.chatbubble.adapter.chat

class Chats {

    private val list: MutableList<ItemChat> = ArrayList()

    companion object {
        private const val INCOMING: Int = 0
        private const val OUTGOING: Int = 1

        private const val SINGLE: Int = 0
        private const val LATEST: Int = 1
        private const val IN_BETWEEN: Int = 2
        private const val OLDEST: Int = 3
    }

    init {
        create(1, "Hai, Rizky! Apa kabar?", INCOMING, SINGLE)

        create(2, "Hai, Sarah! Kabar baik.", OUTGOING, OLDEST)
        create(3, "Eh, denger-denger kamu beli handphone baru, ya?", OUTGOING, LATEST)

        create(4, "Iya nih, Rizky! Aku baru saja membeli iPhone terbaru.", INCOMING, OLDEST)
        create(5, "Keren banget desainnya!", INCOMING, LATEST)

        create(6, "Wah, serius?", OUTGOING, OLDEST)
        create(7, "Aku juga sedang mencari handphone baru.", OUTGOING, IN_BETWEEN)
        create(8, "Kenapa kamu pilih iPhone?", OUTGOING, LATEST)

        create(9, "Suka aja sama ekosistemnya, Rizky.", INCOMING, OLDEST)
        create(10, "Selain itu, kameranya bagus banget!", INCOMING, LATEST)

        create(11, "Hmmm, menarik. Aku masih bingung antara iPhone atau Android. Ada saran?", OUTGOING, SINGLE)

        create(12, "Kalau suka desain simpel", INCOMING, OLDEST)
        create(13, "Integrasi dengan produk Apple lain, coba aja iPhone.", INCOMING, IN_BETWEEN)
        create(14, "Tapi Android juga oke.", INCOMING, LATEST)

        create(15, "Oke, makasih sarannya, Sarah!", OUTGOING, OLDEST)
        create(16, "Nanti aku cek lagi di toko.", OUTGOING, LATEST)

        create(17, "Oke Rizky, semoga beruntung.", INCOMING, SINGLE)

        list.reverse()
    }

    private fun create(id: Int, message: String?, type: Int, condition: Int) {
        list.add(ItemChat(id, message, type, condition))
    }

    fun getList(): MutableList<ItemChat> = list

}