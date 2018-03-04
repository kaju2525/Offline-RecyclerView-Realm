package karun.com.offlinedatabaserecyclearview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import karun.com.offlinedatabaserecyclearview.activity.ZoomActivity
import karun.com.offlinedatabaserecyclearview.model.Model
import java.util.*


class DataAdapter(private val context: Context, private val list: ArrayList<Model>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    var imageLoader: ImageLoader? = null
    var options: DisplayImageOptions? = null

    init {
        imageLoader = ImageLoader.getInstance()
        imageLoader!!.init(ImageLoaderConfiguration.createDefault(context))
        options = DisplayImageOptions.Builder()
                //.showImageForEmptyUri(R.mipmap.ic_launcher)
               // .showImageOnFail(R.mipmap.ic_launcher)
                .resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(FadeInBitmapDisplayer(300))
                .build()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): DataAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_layout, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

       // viewHolder.tv.text = list[i].id4

       // Picasso.with(context).load(list[i].avatar).into(viewHolder.img)

        val url = list[i].avatar
        try {
            if (url.length > 5) {
                viewHolder.img.setVisibility(View.VISIBLE)
                loadImage(viewHolder.img, url, viewHolder.pb)
            } else {
                if (url == "NO") {
                    viewHolder.img.setVisibility(View.INVISIBLE)
                } else {
                    viewHolder.img.setVisibility(View.GONE)
                }
            }
        } catch (e: Exception) {
          //  Logger.show(e)
        }

        viewHolder.itemView.setBackgroundColor(Color.WHITE)

        viewHolder.card.setOnClickListener {

            val i=Intent(context,ZoomActivity::class.java)
            i.putExtra("avatar",url)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            context.startActivity(i)

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun loadImage(imageView: ImageView?, loadURL: String, pb: ProgressBar?) {
        imageLoader!!.displayImage(loadURL, imageView, options, object : SimpleImageLoadingListener() {
            override fun onLoadingStarted(imageUri: String, view: View) {
                pb!!.setVisibility(View.VISIBLE)
            }

            override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                var message: String? = null
                when (failReason) {
                //    IO_ERROR -> message = "Input/Output error"
                  //  OUT_OF_MEMORY -> message = "Out Of Memory error"
                   // NETWORK_DENIED -> message = "Downloads are denied"
                   // UNSUPPORTED_URI_SCHEME -> message = "Unsupported URI scheme"
                  //  UNKNOWN -> message = "Unknown error"
                }
             //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }

            override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                pb!!.setVisibility(View.INVISIBLE)
            }
        })

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {




        //internal var tv: TextView = view.findViewById<View>(R.id.tv_android) as TextView
        internal var card: CardView = view.findViewById<View>(R.id.card) as CardView
        internal var img: ImageView = view.findViewById<View>(R.id.img_android) as ImageView
        internal var pb: ProgressBar  = view.findViewById<View>(R.id.pb) as ProgressBar






    }


}