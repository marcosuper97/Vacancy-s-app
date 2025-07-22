package ru.practicum.android.diploma.ui.region

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.presentation.region.RegionViewModel

class RegionFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null
    private val binding: FragmentRegionBinding get() = _binding!!
    private var adapter: AreasAdapter? = null
    private val viewModel: RegionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AreasAdapter(::onItemClick)
        binding.regionUnput.setOnTouchListener(::handleTouchEvent)
        binding.regionRev.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.regionRev.adapter = adapter
        binding.regionUnput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.regionUnput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_search_24,
                        0
                    )
                    viewModel.searchRegion(CLEAN_REQUEST)
                } else {
                    binding.regionUnput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.cross,
                        0
                    )
                    viewModel.searchRegion(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderUi(state)
            }
        }

        binding.toolbarRegion.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun renderUi(state: RegionState) {
        Log.d("fragment", state.toString())
        when (state) {
            is RegionState.Content -> {
                adapter?.update(state.areas)
                hidePlaceholder()
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.VISIBLE
            }

            RegionState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.GONE
                showPlaceholder(ERROR)
            }

            RegionState.Loading -> {
                binding.regionRev.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                hidePlaceholder()
            }

            RegionState.NotFound -> {
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.GONE
                showPlaceholder(NOT_FOUND)
            }

            is RegionState.Search -> {
                hidePlaceholder()
                adapter?.update(state.areas)
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.VISIBLE
            }
        }
    }

    private fun showPlaceholder(boolean: Boolean) {
        if (boolean) {
            binding.errorText.text = getString(R.string.couldnt_get_list)
            binding.regionPlaceholder.setImageResource(R.drawable.error_carpet_placeholder)
            binding.errorText.visibility = View.VISIBLE
            binding.regionPlaceholder.visibility = View.VISIBLE
        } else {
            binding.errorText.text = getString(R.string.region_is_not_found)
            binding.regionPlaceholder.setImageResource(R.drawable.error_cat_placeholder)
            binding.errorText.visibility = View.VISIBLE
            binding.regionPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun hidePlaceholder() {
        binding.regionPlaceholder.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    private fun handleTouchEvent(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP &&
            event.rawX >= binding.regionUnput.right - binding.regionUnput.compoundPaddingEnd
        ) {
            viewModel.searchRegion(CLEAN_REQUEST)
            binding.regionUnput.text.clear()
            return true
        }
        return false
    }

    private fun onItemClick(area: Areas) {
        viewModel.regionUpdate(area)
        findNavController().navigateUp()
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
