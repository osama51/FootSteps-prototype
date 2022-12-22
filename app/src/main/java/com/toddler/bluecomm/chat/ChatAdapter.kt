package com.toddler.bluecomm.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.toddler.bluecomm.R
import com.toddler.bluecomm.databinding.ItChatbubbleBinding
import kotlinx.android.synthetic.main.it_chatbubble.view.*

class ChatAdapter(val context: Context, private val clickListener: ChatBubbleListener) :
    ListAdapter<ChatBubble, ChatAdapter.ViewHolder>(DiffCallback()) {

    inner class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

//    private var context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.it_chatbubble, parent, false)
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: ItChatbubbleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListener: ChatBubbleListener, chatBubble: ChatBubble) {
            binding.chatBubble = chatBubble
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItChatbubbleBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class DiffCallback() : DiffUtil.ItemCallback<ChatBubble>() {
        override fun areItemsTheSame(oldItem: ChatBubble, newItem: ChatBubble): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatBubble, newItem: ChatBubble): Boolean {
            return oldItem == newItem
        }
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = getItem(position)
        holder.bind(clickListener, chatItem)
        holder.itemView.apply {
            message_text.text = chatItem.chatMessage
            message_date.text = chatItem.messageDate

            setOnClickListener {
                it.message_date.visibility = when(it.message_date.visibility){
                    VISIBLE -> GONE
                    else -> {VISIBLE}
                }
            }
        }
    }

    class ChatBubbleListener(val clickListener: (chatBubble: ChatBubble) -> Unit) {
        fun onClick(chatBubble: ChatBubble) = clickListener(chatBubble)
    }
}