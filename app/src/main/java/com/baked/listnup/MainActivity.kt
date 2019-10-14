package com.baked.listnup

import android.util.Log
import android.os.Bundle
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Callback
import okhttp3.Call
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.DividerItemDecoration
import java.nio.file.Files.find
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {
    val homeTitles: ArrayList<String> = ArrayList()
//    var homeList: RecyclerView? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val v = findViewById<RecyclerView>(R.id.home_list)
        addTitles()

        home_list.layoutManager = LinearLayoutManager(this)
        fun appClickListener(position: Int) {
            val intent = Intent(this, List::class.java)
            intent.putExtra("ListTitle",  homeTitles[position])
            startActivity(intent)

            Toast.makeText(this@MainActivity, position.toString() + " is clicked..." + "which is " + homeTitles[position], Toast.LENGTH_LONG).show()
        }
        val listener = { i: Int -> appClickListener(i) }
        home_list.adapter = HomeAdapter( homeTitles, this, listener)
        home_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        initSwipe(v)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
//
//            Toast.makeText(this@MainActivity, "FAB is clicked...", Toast.LENGTH_LONG).show()
//            val intent = Intent(this, List::class.java)
//            startActivity(intent)
//            var responseText: String
//            val responseText = "Yo!"
//            textView.text = responseText//.toString()
//run()
        }
    }

    private fun initSwipe(view: View) {
        val background = ColorDrawable(Color.GRAY)
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT
                or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT
                or ItemTouchHelper.RIGHT){
            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
//                Log.d("ListnUp", homeTitles.toString())

                val from = p1.layoutPosition
                val to = p2.layoutPosition
                Collections.swap(homeTitles, from, to)
//                Log.d("ListnUp", homeTitles.toString())

                home_list.adapter?.notifyItemMoved(from, to)
                home_list.adapter?.notifyItemChanged(from)
                home_list.adapter?.notifyItemChanged(to)

//                Log.d("ListnUp", p1.adapterPosition.toString())
//                Log.d("ListnUp", p2.adapterPosition.toString())

                return false
            }

            override fun onSwiped(p0: RecyclerView.ViewHolder, p1: Int) {
                val position = p0.adapterPosition
                val tmp = homeTitles[position]
                homeTitles.removeAt(position)
                home_list.adapter?.notifyItemRemoved(p0.layoutPosition)//(p0.adapterPosition)
                home_list.adapter?.notifyItemRangeChanged(p0.layoutPosition, getItemCount() - p1)

                Snackbar.make(view, "List deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        homeTitles.add(position-1, tmp)
                        home_list.adapter?.notifyDataSetChanged()
                    }.show()

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {


                super.onChildDraw(
                    c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                if (dX > 0) { // Swiping to the right
                    background.setBounds(
                        itemView.left + dX.toInt(), itemView.top,
                        itemView.left,
                        itemView.bottom
                    )

                } else if (dX < 0) { // Swiping to the left
                    background.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top, itemView.right, itemView.bottom
                    )
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }
                background.draw(c)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(home_list)
    }

    fun getItemCount(): Int {
        return homeTitles.size
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun run(){
        val request = Request.Builder()
            .url("http://10.0.0.164:80/index.php")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response){
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val resp = response.body!!.string()
//                    for ((name, value) in response.headers) {
//                        println("$name: $value")
//                    }
//                    resp = response.body!!.string()
//                    println(resp)
//                    println(response.body!!.string())
//                    textView?.text ="test" //resp
                    this@MainActivity.runOnUiThread(Runnable {
                        //textView?.text = resp
                    })
                }
            }
        })
    }

    fun addTitles() {
        homeTitles.add("dog")
        homeTitles.add("cat")
        homeTitles.add("owl")
        homeTitles.add("cheetah")
        homeTitles.add("raccoon")
        homeTitles.add("bird")
        homeTitles.add("snake")
        homeTitles.add("lizard")
        homeTitles.add("hamster")
        homeTitles.add("bear")
        homeTitles.add("lion")
        homeTitles.add("tiger")
        homeTitles.add("horse")
        homeTitles.add("frog")
        homeTitles.add("fish")
        homeTitles.add("shark")
        homeTitles.add("turtle")
        homeTitles.add("elephant")
        homeTitles.add("cow")
        homeTitles.add("beaver")
        homeTitles.add("bison")
        homeTitles.add("porcupine")
        homeTitles.add("rat")
        homeTitles.add("mouse")
        homeTitles.add("goose")
        homeTitles.add("deer")
        homeTitles.add("fox")
        homeTitles.add("moose")
        homeTitles.add("buffalo")
        homeTitles.add("monkey")
        homeTitles.add("penguin")
        homeTitles.add("parrot")

        home_list.adapter?.notifyDataSetChanged()

    }
}
