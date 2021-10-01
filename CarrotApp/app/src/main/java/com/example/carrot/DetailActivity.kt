package com.example.carrot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        findViewById<ImageButton>(R.id.detail_back).setOnClickListener {
            finish()
        }
        val id = intent.extras?.getInt("id")
        val scroll = findViewById<LinearLayout>(R.id.detail_scroll_linear)
        val client = OkHttpClient()
        client.newCall(Request.Builder().url("http://10.0.2.2:3000/detail?id=" + id.toString()).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DY", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Thread {
                    val jsonObject: JSONObject = JSONObject(response.body?.string())
                    val dataArray: JSONArray = jsonObject.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val format = SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'")
                        val date = format.parse(dataObject.getString("date"))

                        runOnUiThread {
                            Glide.with(this@DetailActivity).load(dataObject.getString("pic")).into(findViewById<ImageView>(R.id.detail_profile_pic))
                            findViewById<TextView>(R.id.detail_profile_nickname).text = dataObject.getString("id")
                            findViewById<TextView>(R.id.detail_profile_location).text = dataObject.getString("location")
                            findViewById<TextView>(R.id.detail_title).text = dataObject.getString("title")
                            findViewById<TextView>(R.id.detail_category).text = UserData.categories[dataObject.getInt("category")] + " · " + SimpleDateFormat("yyyy-MM-dd").format(date)
                            findViewById<TextView>(R.id.detail_desc).text = dataObject.getString("descr")
                            findViewById<TextView>(R.id.detail_bar_price).text = NumberFormat.getInstance(Locale.getDefault()).format(dataObject.getInt("price")) + "원"
                        }
                    }
                }.start()
            }
        })
        client.newCall(Request.Builder().url("http://10.0.2.2:3000/detail_pic?id=" + id.toString()).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("DY", e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                Thread {
                    val jsonObject: JSONObject = JSONObject(response.body?.string())
                    val dataArray: JSONArray = jsonObject.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        runOnUiThread {
                            val img: ImageView = ImageView(this@DetailActivity)
                            Glide.with(this@DetailActivity).load(dataObject.getString("url")).placeholder(R.drawable.ic_launcher_background).into(img)
                            img.layoutParams = LinearLayout.LayoutParams(scroll.height, scroll.height)
                            img.adjustViewBounds = true
                            img.scaleType = ImageView.ScaleType.FIT_CENTER
                            scroll.addView(img)
                        }
                    }
                }.start()
            }
        })
    }
}