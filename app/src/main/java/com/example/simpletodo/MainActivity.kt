package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileSystemUtils
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {

                //Remove Item
                listOfTasks.removeAt(position)
                //Notify the adapter that the SET data changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        loadItems()

        //Look for recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        //Attaches the adapter to the recyclerView to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Button Setup
        findViewById<Button>(R.id.button).setOnClickListener{
            val userInputtedTask = inputTextField.text.toString()
        //Add String to our list
            listOfTasks.add(userInputtedTask)
            //Notify the adapter data has been updated
            adapter.notifyItemInserted(listOfTasks.size-1)
            //Reset text Field
            inputTextField.setText("")

            saveItems()
        }
    }
    //Save data user inputted
    //Save data by writing and reading a file

    //Get File
    fun  getDataFile() : File{

        //Every line represents an specific task
        return File(filesDir, "data.txt")
    }


    // Load Items be reading every line in the file
    fun loadItems(){
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch(ioException: IOException){
            ioException.printStackTrace()
        }


    }
    //Save items
    fun saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}