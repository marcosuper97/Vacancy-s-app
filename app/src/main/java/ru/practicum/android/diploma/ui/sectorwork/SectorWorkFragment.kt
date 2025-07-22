package ru.practicum.android.diploma.ui.sectorwork

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSectorWorkBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.sectorwork.SectorWorkViewModel

class SectorWorkFragment : Fragment() {
    private var _binding: FragmentSectorWorkBinding? = null
    private val binding: FragmentSectorWorkBinding get() = _binding!!
    private var adapter: IndustriesAdapter? = null
    private val viewModel: SectorWorkViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectorWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = IndustriesAdapter(::onItemClick)
        binding.industryUnput.setOnTouchListener(::handleTouchEvent)
        binding.industriesRev.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.industriesRev.adapter = adapter
        binding.industryUnput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println(s)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.industryUnput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_search_24,
                        0
                    )
                    viewModel.searchIndustry(CLEAN_REQUEST)
                } else {
                    binding.industryUnput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.cross,
                        0
                    )
                    viewModel.searchIndustry(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                println(s)
            }

        })

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderUi(state)
            }
        }

        binding.toolbarSectorWork.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.applyIndustry.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun renderUi(state: IndustryState) {
        when (state) {
            is IndustryState.Content -> {
                binding.applyIndustry.visibility = showApplyButton(state.showApplyButton)
                adapter?.update(state.areas)
                hidePlaceholder()
                binding.progressBar.visibility = View.GONE
                binding.industriesRev.visibility = View.VISIBLE
            }

            is IndustryState.Error -> {
                binding.applyIndustry.visibility = showApplyButton(state.showApplyButton)
                binding.progressBar.visibility = View.GONE
                binding.industriesRev.visibility = View.GONE
                showPlaceholder(ERROR)
            }

            is IndustryState.Loading -> {
                binding.applyIndustry.visibility = showApplyButton(state.showApplyButton)
                binding.industriesRev.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                hidePlaceholder()
            }

            is IndustryState.NotFound -> {
                binding.applyIndustry.visibility = showApplyButton(state.showApplyButton)
                binding.progressBar.visibility = View.GONE
                binding.industriesRev.visibility = View.GONE
                showPlaceholder(NOT_FOUND)
            }

            is IndustryState.Search -> {
                binding.applyIndustry.visibility = showApplyButton(state.showApplyButton)
                hidePlaceholder()
                adapter?.update(state.areas)
                binding.progressBar.visibility = View.GONE
                binding.industriesRev.visibility = View.VISIBLE
            }
        }
    }

    private fun showApplyButton(boolean: Boolean): Int {
        return if (boolean) View.VISIBLE else View.GONE
    }

    private fun showPlaceholder(boolean: Boolean) {
        if (boolean) {
            binding.errorText.text = getString(R.string.couldnt_get_list)
            binding.industriesPlaceholder.setImageResource(R.drawable.error_carpet_placeholder)
            binding.errorText.visibility = View.VISIBLE
            binding.industriesPlaceholder.visibility = View.VISIBLE
        } else {
            binding.errorText.text = getString(R.string.no_such_branches)
            binding.industriesPlaceholder.setImageResource(R.drawable.error_cat_placeholder)
            binding.errorText.visibility = View.VISIBLE
            binding.industriesPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun hidePlaceholder() {
        binding.industriesPlaceholder.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    private fun handleTouchEvent(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP &&
            event.rawX >= binding.industryUnput.right - binding.industryUnput.compoundPaddingEnd
        ) {
            viewModel.searchIndustry(CLEAN_REQUEST)
            binding.industryUnput.text.clear()
            return true
        }
        return false
    }

    private fun onItemClick(industry: Industry) {
        viewModel.industryUpdate(industry)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLEAN_REQUEST = ""
        private const val NOT_FOUND = false
        private const val ERROR = true
    }
}
