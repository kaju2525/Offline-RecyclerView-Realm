package karun.com.offlinedatabaserecyclearview

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import io.realm.Realm
import karun.com.offlinedatabaserecyclearview.model.Model
import karun.com.offlinedatabaserecyclearview.model.Realm_Model
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener  {



    override fun onRefresh() {
        initViews()
    }


    private var realm: Realm? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()


        Realm.init(this)
        realm = Realm.getDefaultInstance()


        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.post({
            swipeRefreshLayout.setRefreshing(true)
            initViews()
        })


    }


    private fun initViews() {

        recyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerview.layoutManager = layoutManager

        swipeRefreshLayout.setRefreshing(true);

        AndroidNetworking.get("http://api.karunkumar.in/apiImageSayariGet").build().getAsJSONObject(object : JSONObjectRequestListener {
            override fun onResponse(response: JSONObject?) {
                val array = response!!.getJSONArray("result")

                val results2 = realm!!.where(Realm_Model::class.java).findAll()
                realm!!.beginTransaction()
                results2.deleteAllFromRealm()
                realm!!.commitTransaction()

                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    val avatar=obj.getString("avatar")

                    var realm_model: Realm_Model? = null
                    val data=avatar.split("~")
                    for(j in 0 until data.size){

                        realm!!.beginTransaction()
                        realm_model = realm!!.createObject(Realm_Model::class.java)
                        realm_model.avatar = data.get(j)
                        realm!!.commitTransaction()

                    }
                }
                var list=ArrayList<Model>()
                val results = realm!!.where(Realm_Model::class.java).findAll()
                for ( rm :Realm_Model in results) {
                    list.add(Model(rm.avatar!!))
                }
                list.reverse()
                val adapter = DataAdapter(applicationContext, list)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }

            override fun onError(anError: ANError?) {

                var list=ArrayList<Model>()
                val results = realm!!.where(Realm_Model::class.java).findAll()
                for ( rm :Realm_Model in results) {
                    list.add(Model(rm.avatar!!))
                }
                list.reverse()
                val adapter = DataAdapter(applicationContext, list)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }

        })




    }


}