package com.qdot.disasteralerter

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qdot.disasteralerter.databinding.ActivityAdminBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.submitButton.setOnClickListener {
            if (validateField()) {
                binding.submitButton.isEnabled = false
                CoroutineScope(Dispatchers.IO).launch {
                    val type = binding.typeText.text.toString()
                    val msg = binding.messageText.text.toString()
                    val time = binding.timeText.text.toString()
                    val client = OkHttpClient()
                    val mediaType = "application/json".toMediaType()

                    val reqBodyData = JSONObject()
                    val subData = JSONObject()
                    subData.put("type",type)
                    subData.put("msg",msg)
                    subData.put("time",time)
                    subData.put("pubTime",getCurrentDate())
                    reqBodyData.put("dataSource","ClusterDrop")
                    reqBodyData.put("database","disaster")
                    reqBodyData.put("collection","alert")
                    reqBodyData.put("document",subData)

                    val request = Request.Builder()
                        .url("https://data.mongodb-api.com/app/data-oageq/endpoint/data/beta/action/insertOne")
                        .header("Content-Type", "application/json")
                        .header("api-key", getString(R.string.apiKey))
                        .post(reqBodyData.toString().toRequestBody(mediaType))
                        .build()

                    runCatching {
                        val response = client.newCall(request).execute()
                        val resStr = response.body!!.string()
                        val obj = JSONObject(resStr)

                        if (obj.has("insertedId")) {
                            withContext(Dispatchers.Main) {
                                binding.submitButton.isEnabled = true
                                Toast.makeText(
                                    this@AdminActivity,
                                    "Alert created successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                binding.submitButton.isEnabled = true
                                Toast.makeText(
                                    this@AdminActivity,
                                    "Failed to create an alert!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
            } else {
                Toast.makeText(
                    this@AdminActivity,
                    "Enter valid data!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun validateField() : Boolean {
        return !(binding.messageText.text == null ||
                binding.typeText.text == null||
                binding.timeText.text == null||
                binding.timeText.text.toString().trim().isEmpty()||
                binding.typeText.text.toString().trim().isEmpty()||
                binding.messageText.text.toString().trim().isEmpty())
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(Date())
    }
}