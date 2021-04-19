package com.example.habittracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentMainBinding
import com.example.habittracker.models.Type
import com.example.habittracker.ui.adapters.HabitListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var habitListAdapter: HabitListAdapter
    private lateinit var viewBinding: FragmentMainBinding
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding = FragmentMainBinding.inflate(layoutInflater)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        habitListAdapter = HabitListAdapter(
            this,
            (activity as MainActivity).storage
        )
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = habitListAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Type.values()[position].toString()
        }.attach()


        floatingActionButton = view.findViewById(R.id.main_btn_create)
        floatingActionButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_habitFragment,
                Bundle().apply
                { putInt("id", -1) })
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).initDrawer()
    }
}