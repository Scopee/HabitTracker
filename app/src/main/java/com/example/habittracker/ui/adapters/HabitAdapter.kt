package com.example.habittracker.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.models.Habit

class HabitAdapter(private var habits: List<Habit>, private val callback: (Int) -> Unit) :
    RecyclerView.Adapter<HabitAdapter.HabitsViewHolder>() {

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
        holder.bind(habits[position])
        holder.itemView.setOnClickListener {
            callback(habits[position].id)
        }
    }

    fun setList(habits: List<Habit>) {
        this.habits = habits
        notifyDataSetChanged()
    }

    class HabitsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val habitTitle: TextView = view.findViewById(R.id.habit_name)
        private val habitDescription: TextView = view.findViewById(R.id.habit_description)
        private val habitPriority: TextView = view.findViewById(R.id.habit_priority)
        private val habitType: TextView = view.findViewById(R.id.habit_type)
        private val habitPeriod: TextView = view.findViewById(R.id.habit_period)
        private val habitColor: ImageView = view.findViewById(R.id.habit_color)

        fun bind(habit: Habit) {
            habitTitle.text = habit.name
            if (habit.description.length > MAX_TEXT_LENGTH)
                habitDescription.text =
                    "${habit.description.subSequence(0,
                        MAX_TEXT_LENGTH
                    )}..."
            else
                habitDescription.text = habit.description
            habitPriority.text = habit.priority.toString()
            habitType.text = habit.type.toString()
            habitPeriod.text = habit.period
            Log.i("Adapter", "bind: color=${habit.color.color}")
            habitColor.setBackgroundResource(habit.color.color)
        }

        companion object {
            private const val MAX_TEXT_LENGTH = 64
        }
    }
}