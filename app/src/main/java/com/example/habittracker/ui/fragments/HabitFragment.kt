package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitBinding
import com.example.habittracker.models.Color
import com.example.habittracker.models.Habit
import com.example.habittracker.models.Priority
import com.example.habittracker.models.Type
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class HabitFragment : Fragment() {

    private var habitId: Int = -1
    private var lastPriority: Priority = Priority.NONE
    private var habit: Habit? = null

    private var viewBinding: FragmentHabitBinding? = null
    private val binding get() = viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).getToolbar().setNavigationOnClickListener {
            checkChanges()
        }
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        arguments?.apply {
            val hid = getInt("id")
            if (hid != -1) {
                habitId = hid
                habit = (activity as MainActivity).storage.find { it.id == hid }!!
                fillFields((activity as MainActivity).storage.find { it.id == hid }!!)
            } else {
                habitId = (activity as MainActivity).storage.size
            }
        }
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

    private fun fillFields(habit: Habit) {
        habitId = habit.id
        binding.editName.setText(habit.name)
        binding.editDescription.setText(habit.description)
        when (habit.type) {
            Type.GOOD -> binding.radioGood.isChecked = true
            Type.BAD -> binding.radioBad.isChecked = true
        }
        binding.editPeriod.setText(habit.period)
        when (habit.color) {
            Color.RED -> binding.radioRed.isChecked = true
            Color.BLUE -> binding.radioBlue.isChecked = true
            Color.GREEN -> binding.radioGreen.isChecked = true
        }
        binding.habitAdd.text = "Update"
        lastPriority = habit.priority
        binding.priorityMenu.editText?.hint = lastPriority.toString()
    }

    private fun checkChanges() {
        val newHabit = createHabitFromFields()
        if (habit == null) {
            if (newHabit.name.isNotEmpty() || newHabit.description.isNotEmpty()) {
                askExit()
            } else {
                (activity as MainActivity).navController.popBackStack()
            }
        } else {
            if (newHabit != habit) {
                askExit()
            } else {
                (activity as MainActivity).navController.popBackStack()
            }
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
        val habit = createHabitFromFields()
        Log.i(TAG, "saveHabit: ${habit.id}")
        if (habit.id == (activity as MainActivity).storage.size) {
            (activity as MainActivity).storage.add(habit)
        } else {
            (activity as MainActivity).storage[habitId] = habit
        }
        (activity as MainActivity).navController.popBackStack()
    }

    private fun createHabitFromFields(): Habit {
        val habitName = binding.editName.text.toString()
        val habitDescription = binding.editDescription.text.toString()
        val habitType = if (binding.radioGood.isChecked) Type.GOOD else Type.BAD
        val habitPriorityView = binding.priorityMenu.editText?.text.toString()
        val habitPriority = if (habitPriorityView.isEmpty()) {
            lastPriority
        } else {
            Priority.valueOf(habitPriorityView)
        }
        val habitPeriod = binding.period.editText?.text.toString()
        val habitColor = when {
            binding.radioRed.isChecked -> Color.RED
            binding.radioBlue.isChecked -> Color.BLUE
            binding.radioGreen.isChecked -> Color.GREEN
            else -> Color.WHITE
        }

        return Habit(
            habitId,
            habitName,
            habitDescription,
            habitPriority,
            habitType,
            habitPeriod,
            habitColor
        )
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        private const val TAG = "HabitFragment"
        fun newInstance(param1: String, param2: String) =
            HabitFragment()
    }
}