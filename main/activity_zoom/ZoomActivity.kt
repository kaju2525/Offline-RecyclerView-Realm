package karun.com.offlinedatabaserecyclearview.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import com.emrekose.pinchzoom.Touch
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import karun.com.offlinedatabaserecyclearview.R
import kotlinx.android.synthetic.main.activity_zoom.*

class ZoomActivity : AppCompatActivity() {
    var imageLoader: ImageLoader? = null
    var options: DisplayImageOptions? = null

    private var url:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zoom)
      //  supportActionBar!!.hide()



        val extras = intent.extras
        if (extras != null) {
            url = extras.getString("avatar");
        }


            imageLoader = ImageLoader.getInstance()
            imageLoader!!.init(ImageLoaderConfiguration.createDefault(applicationContext))
            options = DisplayImageOptions.Builder()
                    //.showImageForEmptyUri(R.mipmap.ic_launcher)
                    // .showImageOnFail(R.mipmap.ic_launcher)
                    .resetViewBeforeLoading()
                    .cacheOnDisc()
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .displayer(FadeInBitmapDisplayer(300))
                    .build()


        try {
            if (url.length > 1) {
                imageView.setVisibility(View.VISIBLE)
                loadImage(imageView, url, pb)
            } else {
                if (url == "NO") {
                    imageView.setVisibility(View.INVISIBLE)
                } else {
                    imageView.setVisibility(View.GONE)
                }
            }
        } catch (e: Exception) {
            //  Logger.show(e)
        }



        imageView.setOnTouchListener(Touch(2f, 6f))


    }


    fun loadImage(imageView: ImageView?, loadURL: String, progress: ProgressBar?) {
        imageLoader!!.displayImage(loadURL, imageView, options, object : SimpleImageLoadingListener() {
            override fun onLoadingStarted(imageUri: String, view: View) {
                progress!!.setVisibility(View.VISIBLE)
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
                progress!!.setVisibility(View.INVISIBLE)
            }
        })

    }
}