package ru.practicum.android.diploma.ui.placework

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentPlaceWorkBinding
import ru.practicum.android.diploma.presentation.placework.PlaceWorkState
import ru.practicum.android.diploma.presentation.placework.PlaceWorkViewModel

class PlaceWorkFragment : Fragment() {

    private var _binding: FragmentPlaceWorkBinding? = null
    private val binding: FragmentPlaceWorkBinding get() = _binding!!
    private val viewModel: PlaceWorkViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderUi(state)
            }
        }
        binding.toolbarPlaceWork.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.countryNavigation.setOnClickListener {
            findNavController().navigate(R.id.action_to_country_fragment)
        }

        binding.regionNavigation.setOnClickListener {
            findNavController().navigate(R.id.action_to_region_fragment)
        }
    }

    private fun textFieldBehavior(
        textInputLayout: TextInputLayout,
        editText: EditText,
        navigationActionId: Int,
        text: String?
    ) {
        if (!text.isNullOrEmpty()) {
            textInputLayout.setEndIconDrawable(R.drawable.cross)
            editText.setText(text)
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(requireContext().getColor(R.color.text_color))
        } else {
            textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(requireContext().getColor(R.color.grey))
            editText.text.clear()
        }
        textInputLayout.setEndIconOnClickListener {
            if (!text.isNullOrEmpty()) {
                editText.text.clear()
                if (navigationActionId == R.id.action_to_country_fragment) {
                    viewModel.deleteCountry()
                } else {
                    viewModel.deleteRegion()
                }
                textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            } else {
                findNavController().navigate(navigationActionId)
            }
        }
    }

    private fun renderUi(state: PlaceWorkState) {
        textFieldBehavior(
            binding.choiceRegion,
            binding.inputRegion,
            R.id.action_to_region_fragment,
            state.area
        )
        textFieldBehavior(
            binding.choiceCountry,
            binding.inputCountry,
            R.id.action_to_country_fragment,
            state.country
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
