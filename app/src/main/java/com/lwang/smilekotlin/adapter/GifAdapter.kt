package com.lwang.smilekotlin.adapter

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.bean.Gif
import com.lwang.smilekotlin.utils.ProgressDownload
import com.lwang.smilekotlin.utils.ProgressListener
import com.lwang.smilekotlin.utils.ScreenUtil
import org.jetbrains.anko.find
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

/**
 * Created by lwang on 17-12-12.
 */
class GifAdapter(var items: List<Gif>?, val recyclerView: RecyclerView) : RecyclerView.Adapter<GifAdapter.MyViewHolder>() {

    var mHeights: MutableMap<Int, Int> = HashMap()
    val options: RequestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    var gifDrawable: GifDrawable? = null


    public fun pauseGif() {
        if (gifDrawable != null) {
            gifDrawable!!.pause()
        }
    }

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                pauseGif()
            }
        })
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Glide.with(holder.gifImageView)
                .asBitmap()
                .load(items?.get(position)?.img)
                .transition(BitmapTransitionOptions().crossFade(800))
                .apply(options)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Bitmap>?, p3: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(bitmap: Bitmap, p1: Any?, p2: Target<Bitmap>?, p3: DataSource?, p4: Boolean): Boolean {
                        val imageViewWidth: Int = ScreenUtil.w(holder.gifImageView.context)
                        val imageViewHeight: Int = ((imageViewWidth.toDouble() / bitmap.width) * bitmap.height).toInt()
                        mHeights.put(position, imageViewHeight)
                        holder.textView.visibility = View.VISIBLE
                        holder.gifImageView.layoutParams.height = imageViewHeight
                        holder.gifImageView.layoutParams.width = imageViewWidth
                        holder.textView.text = items?.get(position)?.title
                        return false
                    }
                })
                .into(holder.gifImageView)

        if (mHeights.containsKey(position)) {
            holder.gifImageView.layoutParams.height = mHeights[position]!!
            holder.textView.text = items?.get(position)?.title
        }

        holder.gifImageView.setOnClickListener {

            pauseGif()

            ProgressDownload.downloadPhoto(items?.get(position)?.img!!, object : ProgressListener {

                override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {

                    holder.progressBar.post {
                        holder.progressBar.visibility = View.VISIBLE
                        holder.progressBar.progress = ((readByte.toFloat() / totalByte) * 100)
                    }
                }

                override fun onSave(filePath: String) {

                    Log.i("wang","----------------------------"+filePath)
                    gifDrawable = GifDrawable(filePath)
                    holder.gifImageView.post {
                        holder.progressBar.visibility = View.INVISIBLE
                        holder.gifImageView.setImageDrawable(gifDrawable)
                    }
                }
            })
        }
    }


    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder? {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false))
    }

    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val gifImageView: GifImageView = item.find(R.id.gifImageView)
        val progressBar: com.github.lzyzsd.circleprogress.DonutProgress = item.find(R.id.progressBar)
        val textView: TextView = item.find(R.id.textView)
    }

}