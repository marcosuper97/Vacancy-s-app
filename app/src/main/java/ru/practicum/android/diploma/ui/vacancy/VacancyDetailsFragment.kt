package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.presentation.vacancy.VacancyDetailsCommand
import ru.practicum.android.diploma.presentation.vacancy.VacancyUiState
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel
import ru.practicum.android.diploma.ui.common.PaddingItemDecoration
import ru.practicum.android.diploma.ui.common.dp
import ru.practicum.android.diploma.ui.common.launchAndRepeatOnLifecycle

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding: FragmentVacancyDetailsBinding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModels()

    private lateinit var adapter: VacancyDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
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
        binding.imSharing.setOnClickListener { viewModel.onShareClick() }

        adapter = VacancyDetailsAdapter()
        binding.contentList.adapter = adapter
        binding.contentList.addItemDecoration(
            PaddingItemDecoration(
                0,
                0,
                16.dp,
                16.dp
            )
        )
        launchAndRepeatOnLifecycle { viewModel.uiState.collect { handleUiState(it) } }
        launchAndRepeatOnLifecycle { viewModel.commands.collect { handleCommands(it) } }
    }

    private fun handleUiState(uiState: VacancyUiState) {
        println("myTag $uiState")
        binding.progressBar.isVisible = uiState.isFetching
        binding.contentList.isVisible = uiState.isContentVisible
        binding.vacancyNotFound.root.isVisible = uiState.isEmptyVisible
        adapter.submitList(uiState.items)
    }

    private fun handleCommands(command: VacancyDetailsCommand) {
        val navController = findNavController()
        when (command) {
            is VacancyDetailsCommand.NavigateBack -> navController.popBackStack()
            is VacancyDetailsCommand.NavigateToShare -> share(command.url)
        }
    }

    private fun share(url: String) {
        val message = url
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "*/*"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(shareIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
