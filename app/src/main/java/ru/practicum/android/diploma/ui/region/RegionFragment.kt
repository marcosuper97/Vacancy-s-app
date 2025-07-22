package ru.practicum.android.diploma.ui.region

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.presentation.region.RegionViewModel
import ru.practicum.android.diploma.ui.country.AreasAdapter
import ru.practicum.android.diploma.ui.country.AreasState

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AreasAdapter(::onItemClick)
        binding.regionRev.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.regionRev.adapter = adapter
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderUi(state)
            }
        }

        binding.toolbarRegion.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun renderUi(state: AreasState) {
        Log.d("fragment", state.toString())
        when (state) {
            is AreasState.Content -> {
                adapter?.countries = state.areas
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.VISIBLE
            }

            AreasState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.regionRev.visibility = View.GONE
                binding.regionPlaceholder.visibility = View.VISIBLE
                binding.errorText.visibility = View.VISIBLE
            }

            AreasState.Loading -> {
                binding.regionRev.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.regionPlaceholder.visibility = View.GONE
                binding.errorText.visibility = View.GONE
            }
        }
    }

    private fun onItemClick(area: Areas) {
        viewModel.regionUpdate(area)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
