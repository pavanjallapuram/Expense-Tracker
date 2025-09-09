package com.example.zobazeexpensetracker.utils

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

object Utils {


    fun simulateExportCsv(ctx: Context, dayTotals: List<Pair<String, Double>>, catTotals: Map<String, Double>) {
// create CSV text
        val csv = StringBuilder()
        csv.append("Date,Total\n")
        dayTotals.forEach { csv.append("${it.first},${"%.2f".format(it.second)}\n") }
        csv.append("\nCategory,Total\n")
        catTotals.forEach { csv.append("${it.key},${"%.2f".format(it.value)}\n") }


// write to cache
        val file = File(ctx.cacheDir, "expense_report_${System.currentTimeMillis()}.csv")
        file.writeText(csv.toString())


// share
        val uri = androidx.core.content.FileProvider.getUriForFile(ctx, ctx.packageName + ".fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        ctx.startActivity(Intent.createChooser(intent, "Share report"))
    }


    fun showSimpleToast(ctx: Context, msg: String) {
        android.widget.Toast.makeText(ctx, msg, android.widget.Toast.LENGTH_SHORT).show()
    }

    fun shareReport(ctx: Context, file: File) {
        val uri = androidx.core.content.FileProvider.getUriForFile(
            ctx,
            ctx.packageName + ".fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        ctx.startActivity(Intent.createChooser(intent, "Share report"))
    }

    fun saveReportToDownloadsAsPdf(
        ctx: Context,
        dayTotals: List<Pair<String, Double>>,
        catTotals: Map<String, Double>
    ): File {
        // Create PDF document
        val pdfDocument = PdfDocument()

        // Create a page (A4 size approx 595x842 points)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas
        val paint = android.graphics.Paint()
        paint.textSize = 14f

        var yPos = 40f

        // Title
        paint.isFakeBoldText = true
        canvas.drawText("Expense Report", 40f, yPos, paint)
        paint.isFakeBoldText = false
        yPos += 40f

        // Day totals
        canvas.drawText("Date Totals:", 40f, yPos, paint)
        yPos += 30f
        dayTotals.forEach {
            canvas.drawText("${it.first}  -  ${"%.2f".format(it.second)}", 60f, yPos, paint)
            yPos += 25f
        }

        yPos += 30f
        canvas.drawText("Category Totals:", 40f, yPos, paint)
        yPos += 30f
        catTotals.forEach {
            canvas.drawText("${it.key}  -  ${"%.2f".format(it.value)}", 60f, yPos, paint)
            yPos += 25f
        }

        pdfDocument.finishPage(page)

        // Save file in Downloads
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val file = File(downloadsDir, "expense_report_${System.currentTimeMillis()}.pdf")
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        pdfDocument.close()
        fos.close()

        Toast.makeText(ctx, "PDF saved in Downloads: ${file.absolutePath}", Toast.LENGTH_LONG).show()

        return file
    }
}