package com.blackblastraven.foruvdice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_title.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class TitleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        //Rollボタン
        moveToRoll.setOnClickListener {
            val intent = Intent(application, SetDiceNumber::class.java)
            startActivity(intent)
        }
        //ホームページにリンク
        accesshome.setOnClickListener {
            //テスト!
            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "database-name3"
            ).build()

            //handler
            val handler = Handler()
            //テスト用データ
            var output = DiceData(-1, -1, -1, -1, -1, -1)
            GlobalScope.launch(Dispatchers.IO) {
                //もし登録しようとしているidが既に存在していたら、追加ではなく更新する。
                if (!db.DiceDataDao().existsCheck(0)) {
                    db.DiceDataDao().createDiceDaia(DiceData(0, 1, 6, 1, 16, 0))
                } else {
                    db.DiceDataDao().updateDiceData(DiceData(0, 1, 6, 1, 16, 0))
                }
                output = db.DiceDataDao().searchFromId(0)
                handler.post {
                    Toast.makeText(
                        applicationContext,
                        "${output.diceId}${output.number}${output.whereUsed}${output.fraction}${output.probability}${output.multiple}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    /*
    handler.postを使った非同期処理、一応成功、ただし全ての処理を別スレッドに記述しているため、スレッド間通信とは呼べない。スレッドAがスレッドBを用意して、その中ですべての処理を行ってしまっている。スレッド間相互通信は出来ていない。
            //handler
            val handler = Handler()
            //テスト用データ
            var output = DiceData(-1, -1, -1, -1, -1, -1)
            GlobalScope.launch(Dispatchers.IO) {
                //もし登録しようとしているidが既に存在していたら、追加ではなく更新する。
                if (!db.DiceDataDao().existsCheck(0)) {
                    db.DiceDataDao().createDiceDaia(DiceData(0, 1, 6, 1, 16, 0))
                } else {
                    db.DiceDataDao().updateDiceData(DiceData(0, 1, 6, 1, 16, 0))
                }
                output = db.DiceDataDao().searchFromId(0)
                handler.post {
                    Toast.makeText(
                        applicationContext,
                        "${output.diceId}${output.number}${output.whereUsed}${output.fraction}${output.probability}${output.multiple}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
     */
    /*
    handler.messageを使った非同期処理、上手く行かず。
            //handler
            val handler = Handler()
            //スレッド間通信用のメッセージ
            var message = Message.obtain()
            //テスト用データ
            var output = DiceData(-1, -1, -1, -1, -1, -1)
            GlobalScope.launch(Dispatchers.IO) {
                if (!db.DiceDataDao().existsCheck(0)) {
                    thread { db.DiceDataDao().createDiceDaia(DiceData(0, 1, 6, 1, 16, 0)) }
                }
                output = db.DiceDataDao().searchFromId(0)
                message.what = 1
                message.obj = Integer.toString(output.number)
                //送信
                handler.sendMessage(message)
                handler.post {
                    //Toast.makeText(applicationContext, "${output.number}", Toast.LENGTH_LONG).show()
                }
                var data = ""
                //受信&表示
            }
            Toast.makeText(applicationContext, message.obj as String, Toast.LENGTH_LONG).show()
     */
}
