package com.stark.xicommachinetest

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageApiAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ImageApiAdapter.ViewHolder>() {
    private val TAG = "ImageApiAdapter"

    var mImageList: MutableList<Image>? = mutableListOf()

    companion object {
        val IMAGE_URL = "IMAGE_URL"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(mContext)
            .load(mImageList?.get(position)?.imageUrl)
            .into(holder.apiImage)

        holder.apiImage.setOnClickListener {
            Intent(
                mContext,
                DetailActivity::class.java
            ).apply {
                this.putExtra(IMAGE_URL, mImageList?.get(position)?.imageUrl)
                mContext.startActivity(this)
            }
        }
    }

    override fun getItemCount(): Int = if (mImageList == null) 0 else mImageList!!.size

    fun updateImageList(imageList: MutableList<Image>) {
        mImageList?.addAll(imageList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val apiImage: ImageView = itemView.findViewById(R.id.api_image_view)
    }
}