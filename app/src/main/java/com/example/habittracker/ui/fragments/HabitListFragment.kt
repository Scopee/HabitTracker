package com.example.habittracker.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListBinding
import com.example.habittracker.models.Habit
import com.example.habittracker.models.Type
import com.example.habittracker.ui.adapters.HabitAdapter

class HabitListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HabitAdapter


    private var viewBinding: FragmentHabitListBinding? = null
    private val binding get() = viewBinding!!

    private lateinit var type: Type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewBinding = FragmentHabitListBinding.inflate(layoutInflater)
        arguments?.takeIf { it.containsKey(HABITS) }?.apply {
            recyclerView = view.findViewById(R.id.recycler_view)
            type = Type.values()[getInt(HABITS)]
            adapter =
                HabitAdapter(getList()) {
                    (activity as MainActivity).navController.navigate(
                        R.id.action_mainFragment_to_habitFragment,
                        Bundle().apply { putInt("id", it) }
                    )
                }
            val manager = LinearLayoutManager(activity)
            recyclerView.layoutManager = manager
            recyclerView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.setList(getList())
    }

    private fun getList(): List<Habit> {
        return (activity as MainActivity).storage.filter { it.type == type }
    }

    companion object {
        private const val HABITS = "habits"
        fun newInstance(position: Int): HabitListFragment {
            val fragment =
                HabitListFragment()
            val bundle = Bundle()
            bundle.putInt(HABITS, position)
            fragment.arguments = bundle
            return fragment
        }

    }
}