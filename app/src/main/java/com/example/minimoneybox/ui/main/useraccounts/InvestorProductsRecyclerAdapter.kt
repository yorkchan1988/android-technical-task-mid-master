package com.example.minimoneybox.ui.main.useraccounts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.minimoneybox.R
import com.example.minimoneybox.databinding.LayoutInvestorProductListItemBinding
import com.example.minimoneybox.models.InvestorProduct


class InvestorProductsRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var investorProducts: List<InvestorProduct> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.getContext())
        val itemBinding = LayoutInvestorProductListItemBinding.inflate(layoutInflater, parent, false)
        return InvestorProductViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return investorProducts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val investorProduct = investorProducts.get(position)
        (holder as InvestorProductViewHolder).bind(investorProduct)
    }

    fun setInvestorProducts(investorProducts: List<InvestorProduct>) {
        this.investorProducts = investorProducts
        notifyDataSetChanged()
    }

    class InvestorProductViewHolder(binding : LayoutInvestorProductListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var binding: LayoutInvestorProductListItemBinding = binding

        fun bind(investorProduct: InvestorProduct) {
            binding.investorProduct = investorProduct
//            binding.executePendingBindings()
        }
    }
}