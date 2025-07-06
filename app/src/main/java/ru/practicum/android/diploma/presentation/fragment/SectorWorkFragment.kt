package ru.practicum.android.diploma.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentSectorWorkBinding

class SectorWorkFragment : Fragment() {

    private var _binding: FragmentSectorWorkBinding? = null
    private val binding: FragmentSectorWorkBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed"}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSectorWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
