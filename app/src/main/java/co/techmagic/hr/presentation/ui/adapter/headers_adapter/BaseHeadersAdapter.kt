package co.techmagic.hr.presentation.ui.adapter.headers_adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.techmagic.hr.R

abstract class BaseHeadersAdapter<H, T : HasHeaderProperty<H>, ViewHolder : BaseHeadersAdapter.BaseHeadersAdapterViewHolder<T>> //H - Type of header identifier
    : RecyclerView.Adapter<ViewHolder>() {

    private var data = arrayListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getItemLayout(viewType), parent, false)
        return createViewHolder(viewType, view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder : ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    @LayoutRes
    abstract fun getItemLayout(viewType : Int) : Int

    abstract fun createViewHolder(viewType : Int, view : View) : ViewHolder

    public fun setData(data: List<T>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T? {
        return if (position >= 0 && position < data.size) {
            data[position]
        } else {
            null
        }
    }

    open class BaseHeadersAdapterViewHolder<T : HasHeaderProperty<*>>(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(property: T) {

        }
    }
}