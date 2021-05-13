package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.models.Type
import com.example.habittracker.ui.adapters.HabitListAdapter
import com.example.habittracker.util.AppTextWatcher
import com.example.habittracker.viewmodel.HabitListViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var habitListAdapter: HabitListAdapter
    private lateinit var floatingActionButton: FloatingActionButton

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var listViewModel: HabitListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(activity as MainActivity) as T
            }
        }).get(HabitListViewModel::class.java)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val botLayout = view.findViewById<ConstraintLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(botLayout)

        habitListAdapter = HabitListAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = habitListAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = Type.values()[position].toString()
        }.attach()

        floatingActionButton = view.findViewById(R.id.main_btn_create)
        floatingActionButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_mainFragment_to_habitFragment)
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                floatingActionButton.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset)
                    .setDuration(0).start()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })

        listViewModel.sortedList.observe(activity as MainActivity, Observer {
            Log.i(TAG, "onViewCreated: ${it.size}")
        })

        initListeners(botLayout)
        (activity as MainActivity).mainFragment = this
    }

    private fun initListeners(view: View) {
        val radioNone = view.findViewById<CheckBox>(R.id.radio_sort_none)
        val radioName = view.findViewById<CheckBox>(R.id.radio_sort_name)
        val radioDate = view.findViewById<CheckBox>(R.id.radio_sort_date)
        radioNone.isChecked = true

        radioDate.setOnCheckedChangeListener { _, isChecked ->
            Log.i(TAG, "initListeners: radioDate")
            listViewModel.sortByDate = isChecked
            if (isChecked)
                radioNone.isChecked = false
        }
        radioName.setOnCheckedChangeListener { _, isChecked ->
            Log.i(TAG, "initListeners: radioName")
            listViewModel.sortByName = isChecked
            if (isChecked)
                radioNone.isChecked = false
        }
        radioNone.setOnCheckedChangeListener { _, isChecked ->
            Log.i(TAG, "initListeners: radioNone")
            listViewModel.sortByNone = isChecked
            if (isChecked) {
                radioDate.isChecked = false
                radioName.isChecked = false
            }
        }

        val filterText = view.findViewById<TextView>(R.id.filter_text)
        filterText.addTextChangedListener(AppTextWatcher {
            val s = filterText.text.toString()
            Log.i(TAG, "initListeners: onTextChange {$s}")
            listViewModel.nameFilter = s
        })
    }

    fun onBackPressed(): Boolean {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).initDrawer()
        (activity as MainActivity).hideKeyboard()
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}