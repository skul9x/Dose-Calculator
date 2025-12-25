package com.skul9x.dosecalculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skul9x.dosecalculator.databinding.ItemDrugBinding

class DrugAdapter(
    private var drugs: List<Drug>,
    private val onDeleteClicked: (Drug) -> Unit,
    private val onItemClicked: (Drug) -> Unit
) : RecyclerView.Adapter<DrugAdapter.DrugViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugViewHolder {
        val binding = ItemDrugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrugViewHolder, position: Int) {
        val drug = drugs[position]
        holder.bind(drug)
    }

    override fun getItemCount(): Int = drugs.size

    fun updateData(newDrugs: List<Drug>) {
        this.drugs = newDrugs
        notifyDataSetChanged()
    }

    inner class DrugViewHolder(private val binding: ItemDrugBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drug: Drug) {
            binding.textDrugName.text = drug.name
            binding.textDrugConcentration.text = "Hàm lượng: ${drug.mg}mg/${drug.ml}ml"
            binding.textDrugDose.text = "Liều dùng: ${drug.dose} mg/kg/ngày"

            binding.btnDelete.setOnClickListener {
                onDeleteClicked(drug)
            }

            // Bắt sự kiện click vào cả card view để sửa
            itemView.setOnClickListener {
                onItemClicked(drug)
            }
        }
    }
}