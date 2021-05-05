package com.example.habittracker.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.habittracker.ui.fragments.HabitListFragment

class HabitListAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return HabitListFragment.newInstance(position)
    }
}