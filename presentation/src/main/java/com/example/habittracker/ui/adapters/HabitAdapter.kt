package com.example.habittracker.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.models.PresentationHabit

class HabitAdapter(
    private val onItemClickCallback: (String) -> Unit,
    private val onButtonClickCallback: (PresentationHabit) -> Unit
) :
    RecyclerView.Adapter<HabitAdapter.HabitsViewHolder>() {

    private var habits: List<PresentationHabit> = listOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitsViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        return HabitsViewHolder(
            inflater.inflate(R.layout.habit_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position], onButtonClickCallback)
        holder.itemView.setOnClickListener {
            onItemClickCallback(habits[position].id)
        }
    }

    fun setList(habits: List<PresentationHabit>) {
        this.habits = habits
        notifyDataSetChanged()
    }

    class HabitsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val habitTitle: TextView = view.findViewById(R.id.habit_name)
        private val habitDescription: TextView = view.findViewById(R.id.habit_description)
        private val habitPriority: TextView = view.findViewById(R.id.habit_priority)
        private val habitType: TextView = view.findViewById(R.id.habit_type)
        private val habitPeriod: TextView = view.findViewById(R.id.habit_period)
        private val habitColor: ImageView = view.findViewById(R.id.habit_color)
        private val habitButton: Button = view.findViewById(R.id.habit_done)

        fun bind(habit: PresentationHabit, callback: (PresentationHabit) -> Unit) {
            habitTitle.text = habit.name
            habitDescription.text = habit.description
            habitPriority.text = habit.priority.toString()
            habitType.text = habit.type.toString()
            habitPeriod.text = habit.period
            Log.i("Adapter", "bind: color=${habit.color.color}")
            habitColor.setBackgroundResource(habit.color.color)
            habitButton.setOnClickListener {
                callback(habit)
            }
        }
    }
}