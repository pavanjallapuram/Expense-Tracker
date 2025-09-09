package com.example.zobazeexpensetracker.screens

import android.app.DatePickerDialog
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zobazeexpensetracker.R
import com.example.zobazeexpensetracker.data.Expense
import com.example.zobazeexpensetracker.ui.theme.InterFontFamily
import com.example.zobazeexpensetracker.ui.theme.PrimaryColor
import com.example.zobazeexpensetracker.ui.theme.SecondaryColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Calendar
import kotlin.collections.component1
import kotlin.collections.component2
import androidx.core.net.toUri
import coil.compose.AsyncImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    expenses: List<Expense>,
    onDelete: (Int) -> Unit,
    groupByCategory: Boolean,
    onToggleGroup: () -> Unit,
    onFilterDate: (Long?) -> Unit
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true
    SideEffect {
        systemUiController.setStatusBarColor(PrimaryColor, darkIcons = useDarkIcons)
    }

    Image(
        painter = painterResource(R.drawable.ic_normal_screen_bg),
        contentDescription = "",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(10.dp, 5.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back_vector),
                contentDescription = "",
                modifier = Modifier.size(40.dp).align(Alignment.CenterStart)
            )

            Text(
                text = "Expenses",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Card content
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.78f)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {

                // 🔹 Filter & Toggle Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp, 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Transaction History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFontFamily
                    )

                    Row {
                        // Date Filter Button
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Filter by Date")
                        }

                        // Group Toggle Button
                        IconButton(onClick = { onToggleGroup() }) {
                            if (groupByCategory) {
                                Icon(Icons.Default.List, contentDescription = "Group by Time")
                            } else {
                                Icon(Icons.Default.List, contentDescription = "Group by Category")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔹 Expenses List
                if (expenses.isEmpty()) {
                    Text(
                        "No expenses found",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                } else {
                    if (groupByCategory) {
                        val groups = expenses.groupBy { it.category }
                        LazyColumn {
                            groups.forEach { (cat, list) ->
                                item {
                                    Text(
                                        cat,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(8.dp, 4.dp)
                                    )
                                }
                                items(list.size) { e ->
                                    ExpenseRowScreenNew(list[e], onDelete)
                                }
                            }
                        }
                    } else {
                        LazyColumn {
                            items(expenses.size) { e -> ExpenseRowScreenNew(expenses[e], onDelete) }
                        }
                    }
                }
            }
        }

        // 🔹 DatePicker Dialog
        if (showDatePicker) {
            val today = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val cal = Calendar.getInstance()
                    cal.set(year, month, day, 0, 0, 0)
                    onFilterDate(cal.timeInMillis)
                    showDatePicker = false
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}










@Composable
fun ExpenseRowScreenNew(expense: Expense, onDelete: (Int) -> Unit){


    Box(modifier = Modifier.fillMaxWidth().height(65.dp)){
        Row(modifier = Modifier.fillMaxHeight().padding(10.dp,0.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .requiredSize(size = 50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = Color(0xfff0f6f5))
                    .padding(all = 10.dp)
            ) {
                AsyncImage(
                    model = expense.receiptUri?.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) },
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .requiredWidth(34.dp)
                        .requiredHeight(30.dp),
                    placeholder = painterResource(R.drawable.ic_list_frame),
                    error = painterResource(R.drawable.ic_list_frame),
                    fallback = painterResource(R.drawable.ic_list_frame)
                )
            }
            Box(
                modifier = Modifier
                    .requiredWidth(width = 58.dp)
                    .requiredHeight(height = 41.dp)
            ) {
                Text(
                    text = expense.title,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ), maxLines = 1
                )
                Text(
                    text = expense.category,
                    color = SecondaryColor,
                    style = TextStyle(
                        fontSize = 13.sp),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(x = 0.dp,
                            y = 25.dp), maxLines = 1)
            }
        }

        Text(
            text = "+ "+expense.amount.toString(),
            color = Color(0xff25a969),
            textAlign = TextAlign.End,
            style = TextStyle(
                fontSize = 18.sp),
            modifier = Modifier.wrapContentSize().align(Alignment.CenterEnd).padding(0.dp,0.dp,10.dp,0.dp))
    }
}




