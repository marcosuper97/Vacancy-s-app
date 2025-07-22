package ru.practicum.android.diploma.ui.country

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
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.domain.models.Areas
import ru.practicum.android.diploma.presentation.country.CountryViewModel
import ru.practicum.android.diploma.ui.region.AreasAdapter

class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding: FragmentCountryBinding get() = _binding!!
    private var adapter: AreasAdapter? = null
    private val viewModel: CountryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AreasAdapter(::onItemClick)
        binding.countryRev.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.countryRev.adapter = adapter
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                renderUi(state)
            }
        }

        binding.toolbarCountry.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun renderUi(state: CountryState) {
        when (state) {
            is CountryState.Content -> {
                adapter?.countries = state.areas
                binding.progressBar.visibility = View.GONE
                binding.countryRev.visibility = View.VISIBLE
            }

            CountryState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.countryRev.visibility = View.GONE
                binding.countryPlaceholder.visibility = View.VISIBLE
                binding.errorText.visibility = View.VISIBLE
            }

            CountryState.Loading -> {
                binding.countryRev.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.countryPlaceholder.visibility = View.GONE
                binding.errorText.visibility = View.GONE
            }
        }
    }

    private fun onItemClick(area: Areas) {
        viewModel.countryUpdate(area.name, area.id)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}
