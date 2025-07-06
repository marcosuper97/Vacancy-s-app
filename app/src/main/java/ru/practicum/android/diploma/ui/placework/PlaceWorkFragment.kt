package ru.practicum.android.diploma.ui.placework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentPlaceWorkBinding

class PlaceWorkFragment : Fragment() {

    private var _binding: FragmentPlaceWorkBinding? = null
    private val binding: FragmentPlaceWorkBinding get() = requireNotNull(_binding) { "Binding wasn't initiliazed" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
