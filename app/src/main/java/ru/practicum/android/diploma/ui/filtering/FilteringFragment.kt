package ru.practicum.android.diploma.ui.filtering

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
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
import ru.practicum.android.diploma.domain.models.Filters
import ru.practicum.android.diploma.presentation.filtering.FilteringViewModel

class FilteringFragment : Fragment() {

    private var _binding: FragmentFilteringBinding? = null
    private val binding: FragmentFilteringBinding get() = _binding!!
    private val viewModel: FilteringViewModel by viewModel()
    private var salaryTextWatcher: TextWatcher? = null

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

        salaryTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println(s)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.updateSalary(s.toString())
                    if (binding.salaryInput.isFocused) {
                        binding.salaryHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                    } else {
                        binding.salaryHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                    binding.deleteCross.visibility = View.VISIBLE
                } else {
                    binding.deleteCross.visibility = View.GONE
                    viewModel.updateSalary(CLEAN_REQUEST)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                println(s)
            }
        }
        binding.salaryInput.addTextChangedListener(salaryTextWatcher)

        binding.deleteCross.setOnClickListener {
            binding.salaryInput.setText(CLEAN_REQUEST)
            viewModel.updateSalary(CLEAN_REQUEST)
            if (binding.salaryInput.isFocused) {
                binding.salaryHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
            } else {
                binding.salaryHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.salary_filed_color))
            }
        }

        binding.salaryInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputMethodManager =
                    requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.salaryInput.windowToken, 0)
                binding.salaryInput.clearFocus()
                if (binding.salaryInput.text.isNotEmpty()) {
                    binding.salaryHint.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                } else {
                    binding.salaryHint.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.salary_filed_color
                        )
                    )
                }
                true
            } else {
                false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.screenState.collect { state ->
                    renderUi(state.filters)
                }
            }
        }

        binding.onlyWithSalary.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateNoSalary()
            }
        }

        binding.salaryCheckBox.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateNoSalary()
            }
        }

        binding.areaNavigation.setOnClickListener {
            findNavController().navigate(R.id.action_to_place_work_fragment)
        }

        binding.sectorNavigation.setOnClickListener {
            findNavController().navigate(R.id.action_to_sector_work_fragment)
        }

        binding.toolbarFiltering.setNavigationOnClickListener {
            findNavController().navigateUp()
        }


        binding.apply.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set("reapplyFilters", true)
            findNavController().navigateUp()
        }

        binding.drop.setOnClickListener {
            lifecycleScope.launch {
                binding.salaryCheckBox.isChecked = false
                viewModel.reset()
            }
        }
    }

    private fun renderUi(state: Filters) {
        if (!state.salary.isNullOrEmpty()) {
            binding.salaryInput.setText(state.salary)
        }
        showButtons(state)
        checkBoxState(state.onlyWithSalary)
        areaFieldBehavior(
            binding.area,
            binding.areaEdit,
            R.id.action_to_place_work_fragment,
            listOf(state.country, state.area),
        )
        industryFieldBehavior(
            binding.sector,
            binding.sectorEdit,
            R.id.action_to_sector_work_fragment,
            state.industry
        )
    }

    private fun Filters.hasAnyNonNull(): Boolean {
        return country != null ||
            countryId != null ||
            area != null ||
            areaId != null ||
            industry != null ||
            industryId != null ||
            salary != null ||
            onlyWithSalary != null
    }

    private fun showButtons(filters: Filters) {
        if (filters.hasAnyNonNull()) {
            binding.drop.visibility = View.VISIBLE
            binding.apply.visibility = View.VISIBLE
        } else {
            binding.drop.visibility = View.GONE
            binding.apply.visibility = View.GONE
        }
    }

    private fun checkBoxState(state: Boolean?) {
        if (state == null) {
            binding.salaryCheckBox.isChecked = false
        } else binding.salaryCheckBox.isChecked = state
    }

    private fun areaFieldBehavior(
        textInputLayout: TextInputLayout,
        editText: EditText,
        navigationActionId: Int,
        textAreas: List<String?>?
    ) {
        textInputLayout.setEndIconDrawable(R.drawable.cross)
        val areasText: String = with(StringBuilder()) {
            textAreas?.get(0)?.let { append(it) }
            textAreas?.get(1)?.let { append(", $it") }
            toString()
        }
        if (areasText.isNotEmpty()) {
            editText.setText(areasText)
            textInputLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.text_color))
        } else {
            textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(requireContext().getColor(R.color.grey))
            editText.text.clear()
        }

        textInputLayout.setEndIconOnClickListener {
            if (!areasText.isNullOrEmpty()) {
                editText.text.clear()
                viewModel.clearAreas()
                textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            } else {
                findNavController().navigate(navigationActionId)
            }
        }
    }

    private fun industryFieldBehavior(
        textInputLayout: TextInputLayout,
        editText: EditText,
        navigationActionId: Int,
        textIndustry: String?
    ) {
        if (!textIndustry.isNullOrEmpty()) {
            textInputLayout.setEndIconDrawable(R.drawable.cross)
            editText.setText(textIndustry)
            textInputLayout.defaultHintTextColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.text_color))
        } else {
            textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            textInputLayout.defaultHintTextColor = ColorStateList.valueOf(requireContext().getColor(R.color.grey))
            editText.text.clear()
        }

        textInputLayout.setEndIconOnClickListener {
            if (!textIndustry.isNullOrEmpty()) {
                editText.text.clear()
                viewModel.clearIndustry()
                textInputLayout.setEndIconDrawable(R.drawable.arrow_forward_24)
            } else {
                findNavController().navigate(navigationActionId)
            }
        }
    }

    companion object {
        private const val CLEAN_REQUEST = ""
    }
}


