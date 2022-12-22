package com.toddler.bluecomm.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatBubble(
    val id: Long, val chatMessage: String, val messageDate: String,
    val sender: String
) : Parcelable