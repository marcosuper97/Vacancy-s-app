package ru.practicum.android.diploma.ui.filtering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilteringBinding
import ru.practicum.android.diploma.presentation.filtering.FilteringScreenState
import ru.practicum.android.diploma.presentation.filtering.FilteringViewModel

class FilteringFragment : Fragment() {

    private var _binding: FragmentFilteringBinding? = null
    private val binding: FragmentFilteringBinding get() = _binding!!
    private val viewModel: FilteringViewModel by viewModel()

    private var areaEdit: TextInputLayout? = null
    private var industryEdit: TextInputLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilteringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()

        binding.toolbarFiltering.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        listOf(
            binding.area,
            binding.areaEdit,
        ).forEach {
            it.setOnClickListener {
                findNavController().navigate(R.id.action_to_place_work_fragment)
            }
        }

        listOf(
            binding.sector,
            binding.sectorEdit,
        ).forEach {
            it.setOnClickListener {
                findNavController().navigate(R.id.action_to_place_work_fragment)
            }
        }

        binding.area.setEndIconOnClickListener {
            viewModel.updateArea(null)
            binding.areaEdit.setText("")
            areaEdit?.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_forward_inset)

        }

        binding.sector.setEndIconOnClickListener {
            viewModel.updateIndustry(null)
            binding.sectorEdit.setText("")
            industryEdit?.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_forward_inset)

        }

        binding.salaryInput.setEndIconOnClickListener {
            viewModel.updateSalary(null)
            binding.salaryEdit.setText("")
        }

        binding.salaryEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.salaryEdit.text.toString() == getString(R.string.enter_the_amount)) {
                binding.salaryEdit.text?.clear()
            }
        }

        binding.salaryCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateOnlyWithSalary(isChecked)
        }

        binding.salaryEdit.addTextChangedListener {
            val text = binding.salaryEdit.text?.toString()
            val salary = text?.toIntOrNull()
            viewModel.updateSalary(salary)
        }

        binding.apply.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.drop.setOnClickListener {
            reset()
        }
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect { state ->
                    render(state)
                }
            }
        }
    }

    private fun render(state: FilteringScreenState) {
        if (state.thereIsFilters) {
            showFilters(state)
        }
    }

    private fun showFilters(state: FilteringScreenState) {
        if (state.filters?.area != null) {
            binding.areaEdit.setText(state.filters.area)
            areaEdit = binding.area
            areaEdit!!.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.cross)
        }
        if (state.filters?.industry != null) {
            binding.sectorEdit.setText(state.filters.industry)
            industryEdit = binding.sector
            industryEdit!!.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.cross)
        }
        if (state.filters?.salary != null) {
            binding.salaryEdit.apply {
                setText(state.filters.salary.toString())
                setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
            }
        }
        binding.salaryCheckBox.isChecked = (state.filters?.onlyWithSalary == true)

        binding.apply.isVisible = true
        binding.drop.isVisible = true
    }

    private fun reset() {

        binding.apply.isVisible = false
        binding.drop.isVisible = false
        viewModel.updateArea(null)
        binding.areaEdit.setText("")
        viewModel.updateIndustry(null)
        binding.sectorEdit.setText("")
        viewModel.clearSalary()
        binding.salaryEdit.setText("")
        viewModel.updateOnlyWithSalary(null)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reset()
            }
        }
    }
}


