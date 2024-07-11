package com.superior.mentallillness.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.superior.mentallillness.R
import com.superior.mentallillness.model.MessageModal

class MessageAdapter(private val context: Context, private val messageList: ArrayList<MessageModal>,private val onItemClick: (MessageModal) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SENDER = 1
        const val VIEW_TYPE_RECEIVER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> SenderViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sender_listitem, parent, false)
            )
            VIEW_TYPE_RECEIVER -> ReceiverViewHolder(
                LayoutInflater.from(context).inflate(R.layout.receiver_listitem, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageData = messageList[position]

        when (holder.itemViewType) {
            VIEW_TYPE_SENDER -> {
                val senderViewHolder = holder as SenderViewHolder
                senderViewHolder.bind(messageData)
            }
            VIEW_TYPE_RECEIVER -> {
                val receiverViewHolder = holder as ReceiverViewHolder
                receiverViewHolder.bind(messageData)
                holder.receiverMessageTextView.setOnClickListener {
                    onItemClick.invoke(messageData)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].USER_MESSAGE) VIEW_TYPE_SENDER else VIEW_TYPE_RECEIVER
    }

    // ViewHolder for Sender
    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessageTextView: TextView = itemView.findViewById(R.id.outgoing_msg)

        fun bind(messageData: MessageModal) {
            senderMessageTextView.text = messageData.message
        }
    }

    // ViewHolder for Receiver
    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverMessageTextView: TextView = itemView.findViewById(R.id.incoming_msg)

        fun bind(messageData: MessageModal) {
            receiverMessageTextView.text = messageData.message
        }
    }
}