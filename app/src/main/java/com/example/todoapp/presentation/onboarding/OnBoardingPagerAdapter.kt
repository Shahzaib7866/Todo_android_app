package com.example.todoapp.presentation.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ItemOnboardingPageBinding

class OnBoardingPagerAdapter(
    private val pages: List<OnBoardingPage>
) : RecyclerView.Adapter<OnBoardingPagerAdapter.PageViewHolder>() {

    inner class PageViewHolder(private val binding: ItemOnboardingPageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(page: OnBoardingPage) {
            binding.imgOnboarding.setImageResource(page.image)
            binding.txtTitle.setText(page.title)
            binding.txtDesc.setText(page.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PageViewHolder(
        ItemOnboardingPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) =
        holder.bind(pages[position])

    override fun getItemCount() = pages.size
}