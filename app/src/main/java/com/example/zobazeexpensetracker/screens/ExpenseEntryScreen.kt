package com.example.zobazeexpensetracker.screens

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zobazeexpensetracker.R
import com.example.zobazeexpensetracker.ui.theme.InterFontFamily
import com.example.zobazeexpensetracker.ui.theme.SecondaryColor
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.zobazeexpensetracker.data.Expense
import com.example.zobazeexpensetracker.ui.theme.PrimaryColor
import com.example.zobazeexpensetracker.utils.Utils.showSimpleToast
import com.example.zobazeexpensetracker.viewmodels.ExpenseViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun ExpenseEntryScreen(onAdd: (Expense) -> Unit, totalToday: Double,viewModel: ExpenseViewModel){

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true  // set false if background is dark

    SideEffect {
        systemUiController.setStatusBarColor(
            color = PrimaryColor, // your desired color
            darkIcons = useDarkIcons
        )
    }


    val focusManager = LocalFocusManager.current

    val defaultCategories = listOf("Staff", "Travel", "Food", "Utility")


    val ctx = LocalContext.current
    var totalAmount by rememberSaveable { mutableDoubleStateOf(totalToday)  }
    var title by rememberSaveable { mutableStateOf("") }
    var amount by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(defaultCategories.first()) }
    var notes by rememberSaveable { mutableStateOf("") }
    var uri by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){
        Image(painter = painterResource(R.drawable.ic_normal_screen_bg), contentDescription = "", modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop)

        Box(modifier = Modifier.fillMaxWidth().height(65.dp).padding(0.dp,0.dp).padding(10.dp,5.dp)){

            Image(painter = painterResource(R.drawable.ic_back_vector), contentDescription = "",
                modifier = Modifier.size(40.dp).align(Alignment.CenterStart))

            Text(
                text = "Add Expense",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center))

        }

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.78f).align(Alignment.Center),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)){


                Column(modifier = Modifier.fillMaxSize().padding(20.dp)){


                    Text(
                        text = "Total Spent Today",
                        color = SecondaryColor,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp), modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally))


                    Text(
                        text = totalToday.toString(),
                        color = Color(0xff222222),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold,
                         modifier = Modifier.fillMaxWidth().padding(0.dp,5.dp).align(Alignment.CenterHorizontally))

                    Text("TITLE",
                        color = SecondaryColor,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium)



                    OutlinedTextField(
                        value = title,
                        onValueChange = { if (it.length <= 50) title = it },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        label = { Text("Enter Your Expense Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )



                    Text("AMOUNT",
                        color = SecondaryColor,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(0.dp,5.dp,0.dp,0.dp))


                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Enter Amount") },
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )



                    Text("CATEGEORY",
                        color = SecondaryColor,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(0.dp,5.dp,0.dp,0.dp))


                    DropDownExample(
                        options = defaultCategories,
                        selectedOption = category,
                        onOptionSelected = { category = it } // 🔑 updates parent state
                    )

                    Text("NOTE(OPTIONAL)",
                        color = SecondaryColor,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(0.dp,5.dp,0.dp,0.dp))


                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Enter Note") },
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Normal
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )

                    Text("INVOICE(OPTIONAL)",
                        color = SecondaryColor,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(0.dp,5.dp,0.dp,0.dp))

                    InVoiceScreen(uri)


                    Button(onClick = {
                        val amount1 = amount.toDoubleOrNull() ?: 0.0
                        if (title.isBlank()) { showSimpleToast(ctx, "Enter title") ; return@Button }
                        if (amount1 <= 0.0) { showSimpleToast(ctx, "Enter valid amount") ; return@Button }
                        val expense = Expense(title = title.trim(), amount = amount1, category = category, notes = if (notes.isBlank()) null else notes, receiptUri = uri)
                        onAdd(expense)
                        title = ""; amount = ""; notes = ""
                        showSimpleToast(ctx, "Added")
                    },
                        modifier = Modifier.fillMaxWidth()
                            .height(60.dp)
                            .padding(0.dp,20.dp,0.dp,0.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = PrimaryColor
                        )) {

                        Text("Submit",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1)


                    }



                }

            }
            // Content inside
        }


    }

}

@Composable
fun InVoiceScreen(uri: String) {

    var imageUri by rememberSaveable { mutableStateOf(uri) }

    val context = LocalContext.current

    // Temporary file to store image
    val photoUri = remember {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "invoice_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    // Launcher for Camera
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it.toString()  // save selected gallery image
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(top = 5.dp)
            .border(
                width = 1.5.dp,
                color = SecondaryColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                // Launch camera
                photoUri?.let { uri ->
                    galleryLauncher.launch("image/*")
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add_invoice),
                contentDescription = ""
            )



            Text(
                text = if (imageUri.isEmpty()) "Add Invoice" else "Invoice Added",
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 5.dp),
                color = SecondaryColor,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }

    // Debug: Show captured image URI string

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownExample(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Option") },
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .height(60.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption) // 🔑 update parent
                        expanded = false
                    }
                )
            }
        }
    }
}





@Composable
fun TotalTodayHeader(total: Double) {
    androidx.compose.material3.Text(
        text = "Total Spent Today: ₹%.2f".format(total),
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
        color = androidx.compose.material3.MaterialTheme.colorScheme.primary
    )
}

