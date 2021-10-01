package com.example.carrot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import okhttp3.*
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            UserData.categories
        )
        findViewById<Spinner>(R.id.upload_category).adapter = adapter

        val client = OkHttpClient()

        if (!intent.extras!!.getBoolean("isUpload")) {
            client.newCall(
                Request.Builder()
                    .url("http://10.0.2.2:3000/detail?id=" + intent.extras?.getInt("id").toString())
                    .build()
            ).enqueue(object :
                Callback {
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
                                Glide.with(this@UploadActivity).load(dataObject.getString("pic"))
                                    .into(findViewById<ImageView>(R.id.detail_profile_pic))
                                findViewById<TextView>(R.id.upload_title).text =
                                    dataObject.getString("title")
                                findViewById<Spinner>(R.id.upload_category).setSelection(
                                    dataObject.getInt(
                                        "category"
                                    )
                                )
                                findViewById<TextView>(R.id.upload_desc).text =
                                    dataObject.getString("descr")
                                findViewById<TextView>(R.id.upload_price).text =
                                    dataObject.getInt("price").toString()
                            }
                        }
                    }.start()
                }
            })
        }

        findViewById<ImageButton>(R.id.upload_back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.upload_accept).setOnClickListener {
            if (intent.extras!!.getBoolean("isUpload")) {
                val rb = FormBody.Builder()
                    .add("userid", "dygames")
                    .add("title", findViewById<TextView>(R.id.upload_title).text.toString())
                    .add(
                        "category",
                        findViewById<Spinner>(R.id.upload_category).selectedItemId.toString()
                    )
                    .add("descr", findViewById<TextView>(R.id.upload_desc).text.toString())
                    .add("price", findViewById<TextView>(R.id.upload_price).text.toString())
                    .build()
                val rq = Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://10.0.2.2:3000/product").post(rb).build()
                client.newCall(rq).enqueue(object :
                    Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("DY", e.message.toString())
                        Toast.makeText(this@UploadActivity, "Upload Failed", Toast.LENGTH_SHORT)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Thread {
                            MainActivity.instance.getProducts()
                            finish()
                        }.start()
                    }
                })
            } else {
                val rb = FormBody.Builder()
                    .add("userid", "dygames")
                    .add("title", findViewById<TextView>(R.id.upload_title).text.toString())
                    .add(
                        "category",
                        findViewById<Spinner>(R.id.upload_category).selectedItemId.toString()
                    )
                    .add("descr", findViewById<TextView>(R.id.upload_desc).text.toString())
                    .add("price", findViewById<TextView>(R.id.upload_price).text.toString())
                    .build()

                client.newCall(
                    Request.Builder().addHeader("Content-Type", "application/x-www-form-urlencoded").url("http://10.0.2.2:3000/product").put(rb).build()
                ).enqueue(object :
                    Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("DY", e.message.toString())
                        Toast.makeText(this@UploadActivity, "Edit Failed", Toast.LENGTH_SHORT)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Thread {
                            finish()
                        }.start()
                    }
                })
            }
        }
    }
}