package com.jew.lab9_2

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var btn_calculate: Button
    private lateinit var ed_height: EditText
    private lateinit var ed_weight: EditText
    private lateinit var tv_weight: TextView
    private lateinit var tv_fat: TextView
    private lateinit var tv_bmi: TextView
    private lateinit var tv_progress: TextView
    private lateinit var progressBar2: ProgressBar
    private lateinit var ll_progress: LinearLayout
    private lateinit var btn_boy: RadioButton
    private var bmiprogress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //將變數與 XML 元件綁定
        btn_calculate = findViewById(R.id.btn_calculate)
        ed_height = findViewById(R.id.ed_height)
        ed_weight = findViewById(R.id.ed_weight)
        tv_weight = findViewById(R.id.tv_weight)
        tv_fat = findViewById(R.id.tv_fat)
        tv_bmi = findViewById(R.id.tv_bmi)
        tv_progress = findViewById(R.id.tv_progress)
        progressBar2 = findViewById(R.id.progressBar2)
        ll_progress = findViewById(R.id.ll_progress)
        btn_boy = findViewById(R.id.btn_boy)
        //對計算按鈕設定監聽器
        btn_calculate.setOnClickListener {
            var msg = Message()
            when {
                ed_height.length() < 1 -> msg.what = 1 //身高
                ed_weight.length() < 1 -> msg.what = 2      //體重
                else ->
                    runAsyncTask()  //執行副程式來執行AsyncTask
            }


        }
    }

    private fun  showtoast(msg:String) =
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()

    private val handler =Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == 1)
            Toast.makeText(this, "輸入身高數值錯誤", Toast.LENGTH_SHORT).show()
        if (msg.what == 2)
            Toast.makeText(this, "輸入體重數值錯誤", Toast.LENGTH_SHORT).show()
        if (msg.what == 3) {
            progressBar2.progress = bmiprogress
            tv_progress.text = "$bmiprogress"
        }
        if (msg.what == 4)
        {
            ll_progress.visibility = View.GONE
            //讀取身高與體重
            val cal_height = ed_height.text.toString().toDouble()   //身高
            val cal_weight = ed_weight.text.toString().toDouble()   //體重
            val cal_standweight: Double
            val cal_bodyfat: Double
            //判斷性別，跳用各自的計算公式
            if (btn_boy.isChecked) {
                cal_standweight = (cal_height - 80) * 0.7
                cal_bodyfat = (cal_weight - 0.88 * cal_standweight) / cal_weight * 100
            } else {
                cal_standweight = (cal_height - 70) * 0.6
                cal_bodyfat = (cal_weight - 0.82 * cal_standweight) / cal_weight * 100
            }
            //顯示計算結果
            tv_weight.text = String.format("標準體重 \n%.2f", cal_standweight)
            tv_bmi.text = String.format("體脂肪 \n%.2f", cal_bodyfat)
        }
        true
    }



    @SuppressLint("StaticFieldLeak")

    private fun runAsyncTask(){
        //初始化『標準體重』與『體脂肪』

        //顯示進度條
        ll_progress.visibility = View.VISIBLE
        thread {
            //初始化進度條
            progressBar2.progress = 0
            tv_progress.text = "0%"
            bmiprogress = 0
            while( bmiprogress < 100 ) {
                Thread.sleep(50)
                bmiprogress++
                var msg =  Message()
                msg.what = 3
                handler.sendMessage(msg)
            }
            var msg =Message()
            msg.what = 4
            handler.sendMessage(msg)
        }.start()


    }

}
