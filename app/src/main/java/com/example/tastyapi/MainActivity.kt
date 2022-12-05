package com.example.tastyapi

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.RequestQueue
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("DEPRECATION")

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    var array = ArrayList<info>()
    lateinit var recycleviewAdapter: RecycleviewAdapter
    lateinit var info: info
    private lateinit var swipeRefershlayout: SwipeRefreshLayout
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycleview)
        array.clear()
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("fetching the data...")
        progressDialog.setTitle("Please wait")
        swipeRefershlayout = findViewById(R.id.refreshLayout)
        loadData()
        swipeRefershlayout.setOnRefreshListener {
            loadData()
            swipeRefershlayout.isRefreshing = false
        }
    }

    private fun loadData() {
        try {
            progressDialog.show()
            val url = "https://tasty.p.rapidapi.com/recipes/list?from=0&size=40"
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val jsonObjectRequest = @RequiresApi(Build.VERSION_CODES.N)
            @SuppressLint("ResourceType", "SimpleDateFormat")
            object : JsonObjectRequest(
                url,
                com.android.volley.Response.Listener {
                    val myFormat = SimpleDateFormat("dd-MM-yyyy")
                    try {
                        for (i in 0..it.getJSONArray("results").length().minus(1)) {
                            val thumbnailUrl = it.getJSONArray("results").getJSONObject(i)
                                .getString("thumbnail_url")
                            val name = it.getJSONArray("results").getJSONObject(i).getString("name")
                            val createdAt =
                                it.getJSONArray("results").getJSONObject(i).getLong("created_at")
                            val newDate = myFormat.format(Date(createdAt * 1000)).toString()
                            info = info(name, thumbnailUrl, newDate)
                            array.add(info)
                        }
                        recycleviewAdapter = RecycleviewAdapter(this, array)
                        recyclerView.adapter = recycleviewAdapter
                        progressDialog.dismiss()
                    } catch (e: Exception) {
                        progressDialog.dismiss()
                        AlertBox(e.message.toString())
                    }
                },
                com.android.volley.Response.ErrorListener { error ->
                    progressDialog.dismiss()
                    AlertBox(error.message.toString())
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["X-RapidAPI-Key"] = "9cb41470fcmsh030f9439fa1ceddp190c93jsnab4873088d07"
                    headers["X-RapidAPI-Host"] = "tasty.p.rapidapi.com"

                    return headers
                }
            }
            jsonObjectRequest.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 500000
                }

                override fun getCurrentRetryCount(): Int {
                    return 500000
                }

                override fun retry(error: VolleyError?) {
                    Toast.makeText(applicationContext, error?.message.toString(), Toast.LENGTH_LONG).show()
                }

            }
            queue.add(jsonObjectRequest)
        } catch (e: Exception) {
            progressDialog.dismiss()
            AlertBox(e.message.toString())
        }
    }

    fun AlertBox(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton(
                "Ok"
            ) { dialog, which -> }
            .setMessage(message)
            .setTitle("Error")
            .setIcon(R.drawable.ic_baseline_error_24)
            .create()
        alertDialog.show()
    }
}
