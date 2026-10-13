
package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SensorScreen()
                }
            }
        }
    }
}

@Composable
fun SensorScreen() {
    var sensorData by remember {

        mutableStateOf<SensorDataClass?>(null)


    }

    val client = remember { OkHttpClient() }
    val scope = rememberCoroutineScope()
    val url = "https://onbgx0grid.execute-api.us-east-1.amazonaws.com/dev/latest"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ESP32 Sensor Monitor",
            style = MaterialTheme.typography.headlineMedium
        )

        Button(
            onClick = {
             //   val raw = RawData(22,33,11,55,66,34)
              //  sensorData = SensorDataClass(temperature=raw.n1,raw.n2,raw.n4,raw.n6)

                scope.launch {
                    val jsonText = withContext(Dispatchers.IO) {
                        val request = Request.Builder()
                            .url(url)
                            .build()

                        val response = client.newCall(request).execute()
                        response.body.string()
                    }

                    val json = JSONObject(jsonText)

                    val raw = RawData(

                                n1 = json.getString("N1").toInt(16),
                                n2 = json.getString("N2").toInt(16),
                                n3 = json.getString("N3").toInt(16),
                                n4 = json.getString("N4").toInt(16),
                                n5 = json.getString("N5").toInt(16),
                                n6 = json.getString("N6").toInt(16),

                    )

                    sensorData= SensorDataClass(
                        temperature = raw.n6,
                        moisture = raw.n2,
                        xVal = raw.n3,
                        yVal = raw.n1

                    )



                    /* val n1 = json.getString("N1").toInt(16)
                     val n2 = json.getString("N2").toInt(16)
                     val n4 = json.getString("N4").toInt(16)
                     val n6 = json.getString("N6").toInt(16)

                     sensorData = SensorDataClass(
                         temperature = n1,
                         moisture = n2,
                         xVal = n4,
                         yVal = n6
                     )


                      */
                }
            },
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        ) {
            Text("Fetch Data")
        }

        /*Text(
            text = sensorData,
            style = MaterialTheme.typography.bodyLarge
        )*/
        if(sensorData==null){

            Text("Temperature: __")
            Text("moisture: __")
            Text("xVal: __")
            Text("yVal: __")
        }
        else{

            Text("Temperature: ${sensorData!!.temperature}", color = if (sensorData!!.temperature > 20) Color.Red else Color.Green, fontSize=24.sp )
            Text("moisture:${sensorData!!.moisture}")
            Text("xVal: ${sensorData!!.xVal}")
            Text("yVal: ${sensorData!!.yVal}")
        }
    }
}


