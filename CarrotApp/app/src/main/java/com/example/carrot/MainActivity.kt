package com.example.carrot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var instance: MainActivity
    }
    var previewItems = mutableListOf<PreviewAdapter.PreviewData>()
    lateinit var scrollView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this

        scrollView = findViewById(R.id.main_scrollView)

        getProducts()

        scrollView.adapter = PreviewAdapter(previewItems)
        scrollView.layoutManager = LinearLayoutManager(this)

        findViewById<View>(R.id.main_fab).setOnClickListener(
            View.OnClickListener {
                val intent = Intent(this, UploadActivity::class.java)
                intent.putExtra("isUpload", true)
                startActivity(intent)
            }
        )

        val client = OkHttpClient()
        client.newCall(Request.Builder().url("http://10.0.2.2:3000/categories").build()).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    Thread {
                        val jsonObject: JSONObject = JSONObject(response.body?.string())
                        val dataArray: JSONArray = jsonObject.getJSONArray("data")
                        for (i in 0 until dataArray.length()) {
                            val dataObject = dataArray.getJSONObject(i)
                            UserData.categories.add(dataObject.getString("title"))
                        }
                    }.start()
                }
            })
    }

    fun getProducts() {
        previewItems.clear()
        val client = OkHttpClient()
        client.newCall(Request.Builder().url("http://10.0.2.2:3000/preview").build())
            .enqueue(object : Callback {
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
                            previewItems.add(
                                PreviewAdapter.PreviewData(
                                    dataObject.getString("url"),
                                    dataObject.getString("title"),
                                    dataObject.getString("location") + " · " + SimpleDateFormat("yyyy-MM-dd").format(
                                        date
                                    ),
                                    dataObject.getInt("price"),
                                    dataObject.getInt("id")
                                )
                            )
                        }

                        Handler(Looper.getMainLooper()).post({ scrollView.adapter?.notifyDataSetChanged() })


                    }.start()
                }
            })
    }
}