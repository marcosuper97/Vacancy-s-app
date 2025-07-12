package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.presentation.vacancy.VacancyUiState
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel
import ru.practicum.android.diploma.ui.common.PaddingItemDecoration
import ru.practicum.android.diploma.ui.common.dp
import ru.practicum.android.diploma.ui.common.launchAndRepeatOnLifecycle

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding: FragmentVacancyBinding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModels()

    private lateinit var adapter: VacancyDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUi()

        binding.toolbarVacancy.let {
            it.setNavigationIcon(R.drawable.ic_arrow_back)
            it.setNavigationOnClickListener { viewModel.onBackClicked() }
        }
    }

    private fun configureUi() {
        adapter = VacancyDetailsAdapter()
        binding.contentList.adapter = adapter
        binding.contentList.addItemDecoration(PaddingItemDecoration(0, 0, 16.dp))
        launchAndRepeatOnLifecycle { viewModel.uiState.collect { handleUiState(it) } }
    }

    private fun handleUiState(uiState: VacancyUiState) {
        println("myTag $uiState")
        binding.progressBar.isVisible = uiState.isFetching
        binding.contentList.isVisible = !uiState.isFetching
        adapter.data = uiState.items
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
