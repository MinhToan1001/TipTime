package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorApp()
        }
    }
}

@Composable
fun TipCalculatorApp() {
    var costOfService by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableStateOf(0.20) }
    var roundUp by remember { mutableStateOf(true) }
    var tipAmount by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tip Time",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary) // Chắc chắn sử dụng background ở đây
                .padding(8.dp)
        )
        // Input cho "Cost of Service" chỉ cho phép nhập số
        TextField(
            value = costOfService,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    costOfService = it // Chỉ cho phép nhập số và dấu chấm
                }
            },
            label = { Text("Cost of Service") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // TextView cho câu hỏi "How was the service?"
        Text("How was the service?")

        // RadioGroup các tùy chọn Tip
        Column {
            TipOptionRow("Amazing (20%)", 0.20, tipPercentage) { tipPercentage = it }
            TipOptionRow("Good (18%)", 0.18, tipPercentage) { tipPercentage = it }
            TipOptionRow("Okay (15%)", 0.15, tipPercentage) { tipPercentage = it }
        }

        // Switch cho "Round up tip?"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Round up tip?")
            Switch(checked = roundUp, onCheckedChange = { roundUp = it })
        }

        // Button "Calculate"
        Button(
            onClick = {
                val cost = costOfService.toDoubleOrNull() ?: 0.0
                var calculatedTip = cost * tipPercentage
                if (roundUp) calculatedTip = ceil(calculatedTip)
                tipAmount = calculatedTip
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        // Luôn hiển thị chữ "Tip Amount"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tip Amount:")
            // Cập nhật số tiền tip khi đã tính toán
            Text(text = if (tipAmount > 0) "$%.2f".format(tipAmount) else "")
        }
    }
}

@Composable
fun TipOptionRow(label: String, percentage: Double, selectedPercentage: Double, onSelected: (Double) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (percentage == selectedPercentage),
            onClick = { onSelected(percentage) }
        )
        Text(label)
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorAppPreview() {
    TipCalculatorApp()
}

