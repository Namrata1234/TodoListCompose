package com.example.todolistcompose

import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistcompose.ui.theme.TodoListComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(){

    val myContext= LocalContext.current
    val focusManager = LocalFocusManager.current

    val clickedItemIndex= remember {
        mutableStateOf(0)
    }

    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }
    val updateDialogStatus = remember {
        mutableStateOf(false)
    }
    val clickItem= remember {
        mutableStateOf("")
    }

    val itemList = readData(myContext)
    val todoName= remember {
        mutableStateOf("")
    }
    val textStatusDialog = remember{
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
       Row(modifier = Modifier
           .fillMaxWidth()
           .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {

           TextField(value = todoName.value, onValueChange = {
               todoName.value = it
           }, label = { Text(text = "Enter TODO") },
               colors = TextFieldDefaults.textFieldColors(
                   focusedIndicatorColor = Color.Transparent,
                   unfocusedIndicatorColor = Color.Transparent,
                   focusedLabelColor = Color.Green,
                   unfocusedLabelColor = Color.White,
                   containerColor = MaterialTheme.colorScheme.primary,
                   focusedTextColor = Color.White,
                   cursorColor = Color.White


               ),
               shape = RoundedCornerShape(5.dp),
               modifier = Modifier
                   .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                   .weight(7f)
                   .height(60.dp),
               textStyle = TextStyle(textAlign = TextAlign.Center)
           )
           Spacer(modifier = Modifier.width(5.dp))
           Button(
               onClick = {
                   if(todoName.value.isNotEmpty()){
                       itemList.add(todoName.value)
                       writeData(itemList,myContext)
                       todoName.value=""
                       focusManager.clearFocus()
                   } else{
                       Toast.makeText(myContext,"Please Enter TODO",Toast.LENGTH_SHORT).show()
                   }
               },
               modifier = Modifier
                   .weight(3f)
                   .height(60.dp),
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color.Green,
                   contentColor = Color.White
               ),
               shape = RoundedCornerShape(5.dp),
               border = BorderStroke(1.dp, Color.Black)
           ) {
               Text(text = "Add", fontSize = 20.sp)
           }
       }

           LazyColumn {
               items(count = itemList.size, itemContent = { index: Int ->
                   ListItemView(itemList[index], index = index, onDeleteClick = {
                   deleteDialogStatus.value = true
                       clickedItemIndex.value=index
               },onUpdateClick={
                    updateDialogStatus.value=true
                       clickedItemIndex.value=index
                       clickItem.value=itemList[index]
                   }, onTextItemClick = {
                       clickItem.value=itemList[index]
                       textStatusDialog.value=true
                   }
                   )
               })

           }
        if(deleteDialogStatus.value){
               AlertDialog(onDismissRequest = { deleteDialogStatus.value=false },
                   title = {Text(text = "Delete")},
                   text = { Text(text = "Do you want to delete this item from List?")},
                   confirmButton = { 
                       TextButton(onClick = {
                           itemList.removeAt(clickedItemIndex.value)
                           writeData(itemList,myContext)
                           deleteDialogStatus.value=false
                           Toast.makeText(myContext,"Item is removed from the list.",Toast.LENGTH_SHORT).show()
                       }) {
                          Text(text = "YES")
                       }
                   },
                   dismissButton = {
                       TextButton(onClick = { deleteDialogStatus.value=false }) {
                           Text(text = "NO")
                       }
                   }
               )
           }
        if(updateDialogStatus.value){
            AlertDialog(onDismissRequest = { updateDialogStatus.value=false },
                title = {Text(text = "Update")},
                text = { TextField(value = clickItem.value, onValueChange = {
                    clickItem.value=it
                })},
                confirmButton = {
                    TextButton(onClick = {
                        itemList[clickedItemIndex.value] =clickItem.value
                        writeData(itemList,myContext)
                        updateDialogStatus.value=false
                        Toast.makeText(myContext,"Item is updated.",Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { updateDialogStatus.value=false }) {
                        Text(text = "NO")
                    }
                }
            )
        }
        if(textStatusDialog.value){
            AlertDialog(onDismissRequest = { textStatusDialog.value=false },
                title = {Text(text = "TODO Item")},
                text = { Text(text = clickItem.value)},
                confirmButton = {
                    TextButton(onClick = {
                       textStatusDialog.value=false
                    }) {
                        Text(text = "OK")
                    }
                },
            )
        }
       }
    }


@Composable
fun ListItemView(item:String,index:Int,onDeleteClick:(item:String)->Unit,
                 onUpdateClick:(item : String) -> Unit,onTextItemClick:(item:String)->Unit){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 1.dp), colors =
    CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White),
        shape = RoundedCornerShape(0.dp)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(text = item, color = Color.White, fontSize = 18.sp,
                maxLines = 2, overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .width(300.dp)
                    .clickable {
                        onTextItemClick(item)
                    })

            Row {
                IconButton(onClick = { onUpdateClick(item)}) {
                    Icon(Icons.Filled.Edit, contentDescription = "edit", tint = Color.White)

                }
                IconButton(onClick = {

                    onDeleteClick(item)
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "delete", tint = Color.White)

                }
            }
        }
    }
}