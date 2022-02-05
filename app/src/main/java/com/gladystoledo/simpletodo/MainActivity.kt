package com.gladystoledo.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
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
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adater that our data has changed
                adapter.notifyDataSetChanged()
                //Save Items
                saveItems()
            }

        }

        //1. Let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener{
//            //Code in here is going to be executed when the user clicks on a button
//            Log.i("Gladys", "User clicked on button")
//        }

        loadItems()

        //Look up recylcerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Set up the button and input flied, so that the user can enter a task
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. Grab the text the user has inputed into @id/addTaskFields
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to our list of task: listOfTasks
            listOfTasks.add(userInputtedTask)

            // 3. Notify the adatper taht our data has updated
            adapter.notifyItemInserted(listOfTasks.size -1)

            // 4. Reset Text Field
            inputTextField.setText("")

            //5. Save Items
            saveItems()
        }
    }

    //Save the data that the user has inputted
    //save the data by writing and reading from a file

    //Create a method to get the files we need
    fun getDataFile() : File{

        //Every line is going to represent a specific task in our list of tasks
        return File(filesDir,"data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioExecption: IOException){
            ioExecption.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}