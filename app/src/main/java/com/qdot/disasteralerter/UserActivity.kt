package com.qdot.disasteralerter

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.google.gson.Gson
import com.qdot.disasteralerter.databinding.ActivityUserBinding
import com.qdot.disasteralerter.model.UpdateData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private var trackRecord : MutableList<UpdateData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (!getUserData()){
            Firebase.messaging.subscribeToTopic("dis-topic")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserData(true)
                    }else{
                        saveUserData(false)
                    }
                }
        }

        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this)

        binding.recyclerView.layoutManager = layoutManager

        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaType()

            val reqBodyData = JSONObject()
            reqBodyData.put("dataSource","ClusterDrop")
            reqBodyData.put("database","disaster")
            reqBodyData.put("collection","alert")

            val request = Request.Builder()
                .url("https://data.mongodb-api.com/app/data-oageq/endpoint/data/beta/action/find")
                .header("Content-Type", "application/json")
                .header("api-key", getString(R.string.apiKey))
                .post(reqBodyData.toString().toRequestBody(mediaType))
                .build()

            runCatching {
                val response = client.newCall(request).execute()
                val resStr = response.body!!.string()
                val obj = JSONObject(resStr)
                val listDoc = obj.getJSONArray("documents")
                for (i in 0 until listDoc.length()) {
                    trackRecord.add(Gson().fromJson(listDoc[i].toString(), UpdateData::class.java))
                }

                withContext(Dispatchers.Main){
                    val rvAdapter = UpdateAdapter(trackRecord)
                    binding.recyclerView.adapter = rvAdapter
                }
            }
        }
    }

    private fun saveUserData(value: Boolean){
        val sharedPref = getSharedPreferences(getString(R.string.fcm), Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("key",value).apply()
    }

    private fun getUserData() : Boolean{
        val sharedPref = getSharedPreferences(getString(R.string.fcm), Context.MODE_PRIVATE)
        return sharedPref.getBoolean("key",false)
    }
}