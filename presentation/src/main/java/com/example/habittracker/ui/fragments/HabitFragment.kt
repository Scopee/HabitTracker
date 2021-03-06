package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitBinding
import com.example.habittracker.models.PresentationHabit
import com.example.habittracker.models.Priority
import com.example.habittracker.viewmodel.HabitViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime
import javax.inject.Inject

class HabitFragment : Fragment() {

    private var habitId: String = ""
    private var lastPriority: Priority = Priority.NONE
    private var habit: PresentationHabit? = null

    private var viewBinding: FragmentHabitBinding? = null
    private val binding get() = viewBinding!!

    @Inject
    lateinit var viewModel: HabitViewModel
    private var new: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            habitId = getString("id")!!
            if (habitId == "-1")
                new = true
            Log.i(TAG, "onCreate: $habitId")
        }
        (activity as MainActivity).appComponent.getHabitComponent().create().inject(this)
        viewModel.init(habitId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_habit,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.habit.observe(viewLifecycleOwner, Observer {
            habit = it
            fillFields(it)
            binding.executePendingBindings()
        })

        (activity as MainActivity).getToolbar().setNavigationOnClickListener {
            checkChanges()
        }
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        initPriorityMenu()
        binding.habitAdd.setOnClickListener {
            saveHabit()
        }
    }

    private fun initPriorityMenu() {
        Log.i(TAG, "initPriorityMenu")
        val priorityMenu = view?.findViewById<TextInputLayout>(R.id.priority_menu)
        val items = Priority.values().map(Priority::toString)
        val adapter = ArrayAdapter<String>(activity as MainActivity, R.layout.priority_item, items)
        val editText = (priorityMenu?.editText as? AutoCompleteTextView)
        editText?.setAdapter(adapter)
    }

    private fun fillFields(habit: PresentationHabit) {
        lastPriority = habit.priority
        binding.priorityMenu.editText?.hint = lastPriority.toString()
        binding.priorityMenu.hint = ""
    }

    private fun checkChanges() {
        if (viewModel.isEmpty()) {
            askExit()
        } else {
            (activity as MainActivity).navController.popBackStack()
        }
    }

    private fun askExit() {
        Snackbar.make(requireView(), "Are you sure want exit without save?", Snackbar.LENGTH_LONG)
            .setAction("Yes") {
                (activity as MainActivity).navController.popBackStack()
            }.show()
    }

    private fun saveHabit() {
        Log.i(TAG, "saveHabit: save")
        val habit = viewModel.habit.value!!
        try {
            habit.priority = Priority.valueOf(binding.priorityMenu.editText?.text.toString())
        } catch (e:Exception) {

        }
        habit.date = LocalDateTime.now()
        viewModel.saveHabit(habit, new)

        (activity as MainActivity).navController.popBackStack()
    }


    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume live: ${viewModel.habit.value}")
    }

    companion object {
        private const val TAG = "HabitFragment"
        fun newInstance() =
            HabitFragment()
    }
}