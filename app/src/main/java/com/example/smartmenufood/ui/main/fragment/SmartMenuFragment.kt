package com.example.smartmenufood.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.smartmenufood.databinding.FragmentSmartMenuBinding
import com.example.smartmenufood.ui.models.SmartMenuViewModel

class SmartMenuFragment : Fragment() {

    private var _binding: FragmentSmartMenuBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val smartMenuViewModel =
            ViewModelProvider(this)[SmartMenuViewModel::class.java]

        _binding = FragmentSmartMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSmartMenu
        smartMenuViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}