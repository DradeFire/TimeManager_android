package com.example.someproj.fragments.task_manager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.someproj.di.App
import com.example.someproj.di.AppComponent
import com.example.someproj.R
import com.example.someproj.adapter.Adapter
import com.example.someproj.databinding.FragmentListBinding
import com.example.someproj.viewmodel.DataModel
import com.example.someproj.roomdb.model.Task
import javax.inject.Inject

val Context.appComponent: AppComponent
    get() = when(this){
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

class ListTaskFragment @Inject constructor() : Fragment() {
    private lateinit var binding: FragmentListBinding
    val dataModel: DataModel by viewModels()
    private lateinit var adapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding
            .inflate(inflater, container, false)

        requireContext().appComponent.inject(this)

        adapter = Adapter(requireContext(), dataModel)
        binding.rcView.adapter = adapter
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    @Inject
    fun startObs(){
        dataModel.readAllData.observe(viewLifecycleOwner, { tasks ->
            adapter.setData(tasks as ArrayList<Task>)
        })

        binding.btSetTask.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_setTaskFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ListTaskFragment()
    }
}