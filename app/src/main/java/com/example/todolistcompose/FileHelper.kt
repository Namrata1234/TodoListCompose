package com.example.todolistcompose

import android.content.Context
import android.view.Display.Mode
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME="todoList.dat"

fun writeData(items:SnapshotStateList<String>,context:Context) {

     val fos= context.openFileOutput(FILE_NAME,Context.MODE_PRIVATE)
    val oas= ObjectOutputStream(fos)
    val itemList = ArrayList<String>()
    itemList.addAll(items)
    oas.writeObject(itemList)
    oas.close()
}

fun readData(context: Context) : SnapshotStateList<String>{

    var itemList=ArrayList<String>()
    try {
        val fis= context.openFileInput(FILE_NAME)
        val ois=ObjectInputStream(fis)
        itemList=ois.readObject() as ArrayList<String>


    }catch (e:FileNotFoundException){
        itemList=ArrayList<String>()

    }
    val items= SnapshotStateList<String>()
    items.addAll(itemList)
    return items


}