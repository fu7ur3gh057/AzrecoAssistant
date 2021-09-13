package az.azreco.azrecoassistant.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.azreco.azrecoassistant.R

class DialogAdapter :
    RecyclerView.Adapter<DialogAdapter.BaseViewHolder<*>>() {

    private val differCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        when (viewType) {
            MSG_TYPE_WEB -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.msg_web_item_list, parent, false)
                WebViewHolder(itemView = view)
            }
            MSG_TYPE_RIGHT -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.msg_item_right, parent, false)
                DialogViewHolder(itemView = view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.msg_item_left, parent, false)
                DialogViewHolder(itemView = view)
            }

        }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = differ.currentList[position]
        when (holder) {
            is WebViewHolder -> holder.bind(element as DialogData.WebLink)
            is DialogViewHolder -> holder.bind(element as DialogData.Message)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class DialogViewHolder(itemView: View) :
        BaseViewHolder<DialogData.Message>(itemView) {
        override fun bind(item: DialogData.Message) {
            val textView = itemView.findViewById<TextView>(R.id.tvShowMessage)
            textView.text = item.text
        }
    }

    inner class WebViewHolder(itemView: View) :
        BaseViewHolder<DialogData.WebLink>(itemView) {
        @SuppressLint("SetJavaScriptEnabled")
        override fun bind(item: DialogData.WebLink) {
            itemView.findViewById<WebView>(R.id.dialogWebView).apply {
                settings.javaScriptEnabled = true
                loadUrl(item.link)
            }
            Log.e("DialogAdapter", item.link)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (val model = differ.currentList[position]) {
            is DialogData.Message -> {
                if (model.isReceived) MSG_TYPE_LEFT
                else MSG_TYPE_RIGHT
            }
            else -> MSG_TYPE_WEB
        }

    companion object {
        private const val MSG_TYPE_LEFT = 0
        private const val MSG_TYPE_RIGHT = 1
        private const val MSG_TYPE_WEB = 2
    }
}

sealed class DialogData {
    class Message(val isReceived: Boolean, val text: String)
    class WebLink(val link: String)
}