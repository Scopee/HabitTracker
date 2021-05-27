package com.example.habittracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.habittracker.MainActivity
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitListBinding
import com.example.habittracker.models.Type
import com.example.habittracker.ui.adapters.HabitAdapter
import com.example.habittracker.viewmodel.HabitListViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class HabitListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HabitAdapter

    @Inject
    lateinit var viewModel: HabitListViewModel

    private var viewBinding: FragmentHabitListBinding? = null
    private val binding get() = viewBinding!!

    private lateinit var type: Type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).appComponent.getListHabitComponent().create().inject(this)
    }

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
        initRecyclerView(view)

        arguments?.takeIf { it.containsKey(HABITS) }?.apply {
            type = Type.values()[getInt(HABITS)]
        }

        viewModel.sortedList.observe(viewLifecycleOwner, Observer { list ->
            Log.i(TAG, "onViewCreated: observe")
            adapter.setList(list.filter { it.type == type })
        })

        val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_container)
        refreshLayout.setOnRefreshListener {
            viewModel.download {
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun initRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        adapter =
            HabitAdapter({
                (activity as MainActivity).navController.navigate(
                    R.id.action_mainFragment_to_habitFragment,
                    Bundle().apply {
                        putString("id", it)
                    }
                )
            }, { viewModel.addDone(it) { it1 ->
                Snackbar.make(requireView(), it1, Snackbar.LENGTH_SHORT ).show()
            } })
        val manager = LinearLayoutManager(activity)
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
    }

    companion object {
        private const val TAG = "HabitListFragment"
        private const val HABITS = "habits"
        fun newInstance(position: Int): HabitListFragment {
            val frg =
                HabitListFragment()
            val bundle = Bundle()
            bundle.putInt(HABITS, position)
            frg.arguments = bundle
            return frg
        }

    }
}