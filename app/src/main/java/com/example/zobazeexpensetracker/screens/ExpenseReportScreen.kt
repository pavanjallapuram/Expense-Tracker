package com.example.zobazeexpensetracker.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zobazeexpensetracker.R
import com.example.zobazeexpensetracker.ui.theme.InterFontFamily
import com.example.zobazeexpensetracker.ui.theme.PrimaryColor
import com.example.zobazeexpensetracker.utils.Utils.saveReportToDownloadsAsPdf
import com.example.zobazeexpensetracker.utils.Utils.simulateExportCsv
import com.example.zobazeexpensetracker.viewmodels.ExpenseViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun FinalReportScreen(viewModel: ExpenseViewModel){

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true  // set false if background is dark

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White, // your desired color
            darkIcons = useDarkIcons
        )
    }

    val ctx = LocalContext.current

    val scope = rememberCoroutineScope()

    var dayTotals by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }
    var catTotals by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    LaunchedEffect(Unit) {
        val (days, cats) = viewModel.last7DaysReport()
        dayTotals = days
        catTotals = cats
    }



    Column(modifier = Modifier.fillMaxSize().background(Color.White)){



        Box(modifier = Modifier.fillMaxWidth().height(65.dp).padding(0.dp,0.dp).padding(10.dp,5.dp)){

            Image(painter = painterResource(R.drawable.ic_back_vector), contentDescription = "",
                modifier = Modifier.size(40.dp).align(Alignment.CenterStart),colorFilter = ColorFilter.tint(Color.Black))

            Text(
                text = "Expense Report",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center))


            Row(modifier = Modifier.align(Alignment.CenterEnd)){
                Image(painter = painterResource(R.drawable.ic_save_vector), contentDescription = "",
                    modifier = Modifier.size(40.dp).padding(6.dp,2.dp).clickable{

                        saveReportToDownloadsAsPdf(ctx, dayTotals, catTotals)


                    })

                Image(painter = painterResource(R.drawable.ic_share), contentDescription = "",
                    modifier = Modifier.size(40.dp).padding(6.dp,2.dp).clickable{

                        simulateExportCsv(ctx, dayTotals, catTotals)

                    },colorFilter = ColorFilter.tint(Color.Black))
            }







        }

        ExpenseReportComposable(viewModel,ctx,dayTotals, catTotals)



    }



}


@Composable
fun BarChartSample(data: List<Pair<String, Double>>){



    if (data.isEmpty()) {
        Text("No data available")
        return
    }

    val maxVal = data.maxOfOrNull { it.second } ?: 1.0

    val bars = data.map { (label, value) ->
        BarChartData.Bar(
            label = label,
            value = value.toFloat(),
            color = PrimaryColor // you can randomize or set based on index
        )
    }

    BarChart(
        barChartData = BarChartData(
            bars = bars
        ), modifier = Modifier.fillMaxWidth().height(300.dp),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(),
        yAxisDrawer = SimpleYAxisDrawer(),
        labelDrawer = SimpleValueDrawer()

    )
}







@Composable
fun ExpenseReportComposable(
    viewModel: ExpenseViewModel,
    ctx: Context,
    dayTotals: List<Pair<String, Double>>,
    catTotals: Map<String, Double>
) {








    Card(modifier = Modifier.fillMaxWidth().padding(15.dp,10.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Last 7 days")
            Spacer(modifier = Modifier.height(8.dp))

            BarChartSample(dayTotals)



            Spacer(modifier = Modifier.height(12.dp))
            Text("By Category")
            catTotals.forEach { (cat, amt) -> Text("$cat : ₹${"%.2f".format(amt)}") }


            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}