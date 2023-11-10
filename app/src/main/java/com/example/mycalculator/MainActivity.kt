package com.example.mycalculator

import java.math.BigDecimal
import java.math.RoundingMode
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    private var strNumber = StringBuilder()
    private lateinit var binding: ActivityMainBinding
    private lateinit var numberDisplay: TextView
    private lateinit var numberButtons: Array<Button>
    private lateinit var buttonDelete: Button
    private var isComaClicked: Boolean = false
    private var isComaAdded: Boolean = false
    private  lateinit var operatorButtons: List<Button>
    private var operator: Operator = Operator.None
    private var isOperatorCLicked: Boolean = false
    private var operand1 = 0.0
    private var operand2 = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InitializeComponnents()
    }
    private fun InitializeComponnents() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        numberDisplay = findViewById(R.id.NumberDisplay)
        numberButtons = arrayOf(binding.button0, binding.button1, binding.button2, binding.button3, binding.button4, binding.button5, binding.button6, binding.button7, binding.button8, binding.button9)
        for (i in numberButtons) { i.setOnClickListener { numberButtonClick(i) } }
        operatorButtons = listOf(binding.buttonPercentage, binding.buttonDecision, binding.buttonAllClear,binding.buttonDivide, binding.buttonTimes, binding.buttonMinus, binding.buttonPlus, binding.buttonSum, binding.buttonComa)
        for (i in operatorButtons)
        {
            i.setOnClickListener { operatorButtonClick(i) }
        }
        binding.buttonComa.setOnClickListener { buttonComaClick() }
        binding.buttonSum.setOnClickListener{buttonSumClick()}
        binding.buttonDelete.setOnClickListener { buttonDeleteClick() }
        binding.buttonAllClear.setOnClickListener {buttonAllClearClick()}
        binding.buttonDecision.setOnClickListener { buttonDecisionCLick() }
        binding.buttonPercentage.setOnClickListener { buttonPercentageClick() }
    }
    private fun buttonPercentageClick() {
        val currentValue = strNumber.toString().toDouble()
        val newValue = currentValue / 100.0
        strNumber.clear()
        strNumber.append(newValue.toString())
        updateDisplay()
    }
    private fun buttonDecisionCLick() {
        try {
            if (strNumber.isNotEmpty()){
                val currentValue = strNumber.toString().toDouble()
                val newValue = -currentValue
                strNumber.clear()
                strNumber.append(newValue.toString())
                updateDisplay()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun buttonComaClick() {
        if(!isComaClicked){
            strNumber.append(".")
            isComaClicked = true
            updateDisplay()
        }
    }
    private fun buttonDeleteClick() {
        if(strNumber.isNotEmpty()){
            strNumber.deleteCharAt(strNumber.length - 1)
            updateDisplay()
        } else {}
    }
    private fun buttonAllClearClick() {
        try{
            strNumber.clear()
            numberDisplay.text = ""
            isComaClicked = false
            if(isOperatorCLicked){
                operand1 = 0.0
                isOperatorCLicked = false
            }
        }
        catch (e: IllegalArgumentException){
            strNumber.clear()
        }
    }
    private fun buttonSumClick() {
        operand2 = strNumber.toString().toDouble()
        var result: BigDecimal
        when(this.operator) {
            Operator.Plus -> result = BigDecimal(operand1.toString()).add(BigDecimal(operand2.toString()))
            Operator.Minus -> result = BigDecimal(operand1.toString()).subtract(BigDecimal(operand2.toString()))
            Operator.Times -> result = BigDecimal(operand1.toString()).multiply(BigDecimal((operand2.toString())))
            Operator.Divide -> {
                if(operand2 != 0.0){
                    result = BigDecimal(operand1.toString()).divide(BigDecimal(operand2.toString()),10, RoundingMode.HALF_EVEN)
                } else{
                    strNumber.clear()
                    numberDisplay.text = "ERROR"
                    return
                }
            }
            else -> result = BigDecimal.ZERO
        }
        strNumber.clear()
        strNumber.append(result.stripTrailingZeros().toPlainString())
        updateDisplay()
        isOperatorCLicked = true
    }
    private fun updateDisplay() {
        try {
            val textValue = strNumber.toString()
            val formattedValue = if(textValue.contains(".0")){textValue.substring(0, textValue.length -2)}
            else{textValue}
            numberDisplay.text = formattedValue
        }
        catch (e: NumberFormatException){
            strNumber.clear()
            numberDisplay.text = "ERROR"
        }
    }
    private fun operatorButtonClick(btn: Button) {
        if (btn.text == "+") operator = Operator.Plus
        else if(btn.text == "-") operator = Operator.Minus
        else if(btn.text == "X") operator = Operator.Times
        else if(btn.text == "/") operator = Operator.Divide
        else operator = Operator.None
        isOperatorCLicked = true
        isComaAdded = false
    }
    private fun numberButtonClick(btn: Button) {
        if(isOperatorCLicked){
            operand1 = strNumber.toString().toDouble()
            strNumber.clear()
            isOperatorCLicked = false
        }
        if(btn.text == ","){
            if(!isComaAdded){
                strNumber.append(btn.text)
                isComaAdded = true
            }
        }else {
            strNumber.append(btn.text)
            updateDisplay()
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MainActivity()
}
enum class Operator{Plus, Minus, Times, Divide,None}