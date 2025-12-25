package com.skul9x.dosecalculator

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.skul9x.dosecalculator.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var drugList = mutableListOf<Drug>()

    // ActivityResultLauncher để nhận kết quả khi màn hình ManageDrugsActivity đóng lại
    private val manageDrugsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // Tải lại danh sách thuốc đã được cập nhật và làm mới UI
            loadAndSetupDrugList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadAndSetupDrugList()
        setupListeners()
    }

    private fun loadAndSetupDrugList() {
        drugList = DrugPersistence.loadDrugs(this)
        val drugNames = drugList.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, drugNames)
        binding.autoCompleteDrug.setAdapter(adapter)
    }

    private fun setupListeners() {
        // Tự động chọn thuốc từ danh sách
        binding.autoCompleteDrug.setOnItemClickListener { parent, _, position, _ ->
            val selectedName = parent.getItemAtPosition(position) as String
            val selectedDrug = drugList.find { it.name == selectedName }
            selectedDrug?.let { populateFields(it) }
        }

        // Các nút bấm
        binding.btnCalculate.setOnClickListener { calculateDose() }
        binding.btnClear.setOnClickListener { clearFields() }
        binding.btnManageDrugs.setOnClickListener {
            val intent = Intent(this, ManageDrugsActivity::class.java)
            manageDrugsLauncher.launch(intent)
        }

        // Tự động tính toán khi người dùng thay đổi thông tin
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateDose()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        binding.inputWeight.addTextChangedListener(textWatcher)

        binding.chipGroupDoses.setOnCheckedChangeListener { _, _ -> calculateDose() }
    }

    private fun populateFields(drug: Drug) {
        binding.inputMg.setText(drug.mg.toString())
        binding.inputMl.setText(drug.ml.toString())
        binding.inputDoseRate.setText(drug.dose.toString())
        calculateDose()
    }

    private fun getDosesPerDay(): Int {
        return when (binding.chipGroupDoses.checkedChipId) {
            R.id.chip_1 -> 1
            R.id.chip_2 -> 2
            R.id.chip_3 -> 3
            R.id.chip_4 -> 4
            else -> 2 // Mặc định
        }
    }

    private fun calculateDose() {
        // Lấy giá trị từ các ô input
        val mg = binding.inputMg.text.toString().toIntOrNull()
        val ml = binding.inputMl.text.toString().toIntOrNull()
        val doseRate = binding.inputDoseRate.text.toString().toIntOrNull()
        val weight = binding.inputWeight.text.toString().toDoubleOrNull()
        val numDoses = getDosesPerDay()

        // Chỉ tính toán khi tất cả các trường cần thiết đều có giá trị hợp lệ
        if (mg == null || ml == null || doseRate == null || weight == null) {
            updateResult(0.0, 0.0)
            return
        }

        if (mg == 0) {
            Toast.makeText(this, "Hàm lượng (mg) không thể bằng 0", Toast.LENGTH_SHORT).show()
            updateResult(0.0, 0.0)
            return
        }

        val totalMlPerDay = (weight * doseRate * ml) / mg
        val mlPerDose = totalMlPerDay / numDoses

        updateResult(mlPerDose, totalMlPerDay)
    }

    private fun updateResult(perDose: Double, totalDay: Double) {
        // Sử dụng Locale.US để đảm bảo dấu chấm thập phân
        binding.textResultPerDose.text = String.format(Locale.US, "%.2f ml", perDose)
        binding.textResultTotalDay.text = String.format(Locale.US, "%.2f ml", totalDay)
    }

    private fun clearFields() {
        binding.autoCompleteDrug.setText("", false) // false để không filter lại dropdown
        binding.inputMg.text?.clear()
        binding.inputMl.text?.clear()
        binding.inputDoseRate.text?.clear()
        binding.inputWeight.text?.clear()
        binding.chipGroupDoses.check(R.id.chip_2)
        updateResult(0.0, 0.0)
        binding.inputMg.requestFocus()
    }
}