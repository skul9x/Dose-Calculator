package com.skul9x.dosecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skul9x.dosecalculator.databinding.ActivityManageDrugsBinding
import com.skul9x.dosecalculator.databinding.DialogAddEditDrugBinding

class ManageDrugsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageDrugsBinding
    private lateinit var drugAdapter: DrugAdapter
    private var drugList = mutableListOf<Drug>()
    private var hasChanges = false // Biến để kiểm tra xem có thay đổi nào không

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageDrugsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drugList = DrugPersistence.loadDrugs(this)

        setupRecyclerView()
        setupListeners()
        checkEmptyState()
    }

    private fun setupRecyclerView() {
        drugAdapter = DrugAdapter(
            drugs = drugList,
            onDeleteClicked = { drug -> confirmDeleteDrug(drug) },
            onItemClicked = { drug -> showAddEditDrugDialog(drug) }
        )
        binding.recyclerViewDrugs.apply {
            adapter = drugAdapter
            layoutManager = LinearLayoutManager(this@ManageDrugsActivity)
        }
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.fabAddDrug.setOnClickListener {
            showAddEditDrugDialog(null) // null nghĩa là thêm mới
        }
    }

    private fun showAddEditDrugDialog(drugToEdit: Drug?) {
        val dialogBinding = DialogAddEditDrugBinding.inflate(LayoutInflater.from(this))
        val isEditing = drugToEdit != null

        val dialogTitle = if (isEditing) "Chỉnh sửa thuốc" else "Thêm thuốc mới"

        // Nếu là chỉnh sửa, điền thông tin cũ vào dialog
        if (isEditing) {
            dialogBinding.inputDialogName.setText(drugToEdit?.name)
            dialogBinding.inputDialogMg.setText(drugToEdit?.mg.toString())
            dialogBinding.inputDialogMl.setText(drugToEdit?.ml.toString())
            dialogBinding.inputDialogDose.setText(drugToEdit?.dose.toString())
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(if (isEditing) "Lưu" else "Thêm") { dialog, _ ->
                val name = dialogBinding.inputDialogName.text.toString().trim()
                val mg = dialogBinding.inputDialogMg.text.toString().toIntOrNull()
                val ml = dialogBinding.inputDialogMl.text.toString().toIntOrNull()
                val dose = dialogBinding.inputDialogDose.text.toString().toIntOrNull()

                if (name.isBlank() || mg == null || ml == null || dose == null) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Kiểm tra tên thuốc trùng lặp (trừ trường hợp đang sửa chính nó)
                val isDuplicate = drugList.any { it.name.equals(name, ignoreCase = true) && it != drugToEdit }
                if (isDuplicate) {
                    Toast.makeText(this, "Tên thuốc này đã tồn tồn tại", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val newDrug = Drug(name, mg, ml, dose)

                if (isEditing) {
                    // Tìm vị trí của thuốc cũ và thay thế
                    val index = drugList.indexOf(drugToEdit)
                    if (index != -1) {
                        drugList[index] = newDrug
                    }
                } else {
                    drugList.add(newDrug)
                }

                sortAndRefreshList()
                dialog.dismiss()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDeleteDrug(drug: Drug) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa thuốc '${drug.name}' không?")
            .setPositiveButton("Xóa") { _, _ ->
                drugList.remove(drug)
                sortAndRefreshList()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun sortAndRefreshList() {
        drugList.sortBy { it.name.lowercase() } // Sắp xếp A-Z
        drugAdapter.updateData(drugList)
        DrugPersistence.saveDrugs(this, drugList)
        hasChanges = true
        checkEmptyState()
    }

    private fun checkEmptyState() {
        binding.textEmptyList.isVisible = drugList.isEmpty()
        binding.recyclerViewDrugs.isVisible = drugList.isNotEmpty()
    }

    override fun finish() {
        // Chỉ trả về RESULT_OK nếu có thay đổi, để MainActivity biết cần cập nhật lại
        if (hasChanges) {
            setResult(RESULT_OK)
        }
        super.finish()
    }
}