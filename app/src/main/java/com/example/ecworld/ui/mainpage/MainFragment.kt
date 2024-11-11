package com.example.ecworld.ui.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecworld.R
import com.example.ecworld.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding:FragmentMainBinding?=null
    private val binding get()=_binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentMainBinding.inflate(inflater,container,false)
        // Inflate the fragment layout
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
