package ru.practicum.android.diploma.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.presentation.main.MainViewModel
import ru.practicum.android.diploma.ui.vacancy.VacanciesAdapter
import ru.practicum.android.diploma.util.SearchVacanciesState
import ru.practicum.android.diploma.util.setVacanciesCountText

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()
    private lateinit var vacanciesAdapter: VacanciesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacanciesAdapter = VacanciesAdapter(::onVacancyClicked)
        binding.mainRv.layoutManager = LinearLayoutManager(context)
        binding.mainRv.adapter = vacanciesAdapter

        binding.toolbarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filtering -> {
                    findNavController().navigate(R.id.action_to_filtering_fragment)
                    true
                }

                else -> false
            }
        }

        //  подписка на vacancies
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vacancies.collectLatest { vacancies ->
                    vacanciesAdapter.submitList(vacancies)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { state ->
                    render(state)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingNextPage.collectLatest { isLoading ->
                    vacanciesAdapter.isLoadingNextPage = isLoading // передаем состояние загрузки в адаптер
                }
            }
        }

        // обработчик изменения текста в поисковой строке
        binding.mainInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.mainInputEt.setCompoundDrawablesRelativeWithIntrinsicBounds( //назначаем иконку "поиска"
                        0,
                        0,
                        R.drawable.ic_search_24,
                        0
                    )
                } else {
                    binding.mainInputEt.setCompoundDrawablesRelativeWithIntrinsicBounds( //назначаем иконку "удаления"
                        0,
                        0,
                        R.drawable.cross,
                        0
                    )
                    viewModel.onSearchTextChanged(s.toString()) // вызываем метод ViewModel
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.mainInputEt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)){ // добавляет обработку для кнопки Enter
                val queryText = binding.mainInputEt.text.toString() // получаем текст из EditText
                viewModel.onSearchTextChanged(queryText)  // вызываем метод ViewModel, который уже содержит дебаунс
                true
            } else {
                false
            }
        }

        // обработчик нажатия на иконку удаления.
        binding.mainInputEt.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.mainInputEt.right - binding.mainInputEt.compoundPaddingEnd)) { // проверка клика по иконке
                    //  клик по иконке (крестик)
                    viewModel.onClearSearchClicked()
                    binding.mainInputEt.text.clear()
                    return@setOnTouchListener true //  Возвращаем true, чтобы обработать клик
                }
            }
            false //  возвращаем false, чтобы разрешить обработку других событий (например, ввод текста)
        }


        binding.mainRv.addOnScrollListener(object : RecyclerView.OnScrollListener() { // добавляем слушатель для прокрутки
            // OnScrollListener() - это интерфейс, который предоставляет методы для реагирования на события прокрутки
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) { // метод вызывается каждый раз, когда происходит прокрутка RecyclerView
                // dx - прокрутка по горизонтали (в нашем случае = 0), dy - прокрутка по вертикали (отрицательное - вверх, положительное - вниз)
                super.onScrolled(recyclerView, dx, dy) // реализация onScrolled() из родительского класса (RecyclerView.OnScrollListener()), позволяет системе обработать стандартные события прокрутки
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount // количество видимых элементов видимых в RecyclerView в данный момент
                val totalItemCount = layoutManager.itemCount // общее количество элементов в списке (всего, с учётом всех загруженных страниц)
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() // позиция (индекс) первого видимого элемента в списке

                // если пользователь достиг конца списка (складываем количество видимых эелментов и позицию первого видимого элемента),
                // то загружаем следующие данные (следующую страницу)
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                    firstVisibleItemPosition >= 0
                ) {
                    // Загружаем следующую страницу
                    viewModel.searchNextPage()
                }
            }
        })
    }

    private fun onVacancyClicked(vacancyId: String) {
        // переход на фрагмент вакансий
    }


    private fun render(state: SearchVacanciesState){
        when(state) {
            SearchVacanciesState.Default -> showDefault()
            SearchVacanciesState.Loading -> showLoading()
            SearchVacanciesState.NetworkError -> showNetworkError()
            SearchVacanciesState.NothingFound -> showNothingFound()
            is SearchVacanciesState.ShowContent -> showContent(state)
            SearchVacanciesState.NoInternet -> showNoInternet()
        }
    }

    fun showDefault(){
        binding.countVacancyTv.isVisible = false
        binding.mainRv.isVisible = false
        binding.mainPb.isVisible = false
        binding.mainDefaultIv.isVisible = true
        binding.errorImageAndMessageLl.isVisible = false

    }

    fun showLoading(){
        binding.countVacancyTv.isVisible = false
        binding.mainRv.isVisible = false
        binding.mainPb.isVisible = true
        binding.mainDefaultIv.isVisible = false
        binding.errorImageAndMessageLl.isVisible = false
    }

    fun showNoInternet(){
        binding.countVacancyTv.isVisible = false
        binding.mainRv.isVisible = false
        binding.mainPb.isVisible = false
        binding.mainDefaultIv.isVisible = false

        binding.errorImageAndMessageLl.isVisible = true
        binding.errorImageIv.setImageResource(R.drawable.image_no_internet)
        binding.errorTextTv.setText(R.string.there_is_no_internet_connection)
    }

    fun showNetworkError() {
        binding.countVacancyTv.isVisible = false
        binding.mainRv.isVisible = false
        binding.mainPb.isVisible = false
        binding.mainDefaultIv.isVisible = false

        binding.errorImageAndMessageLl.isVisible = true
        binding.errorImageIv.setImageResource(R.drawable.image_error_server)
        binding.errorTextTv.setText(R.string.server_error)
    }

    fun showNothingFound(){
        binding.countVacancyTv.isVisible = true
        binding.countVacancyTv.setText(R.string.no_such_vacancies)
        binding.mainRv.isVisible = false
        binding.mainPb.isVisible = false
        binding.mainDefaultIv.isVisible = false
        binding.errorImageAndMessageLl.isVisible = true
    }

    fun showContent(state: SearchVacanciesState.ShowContent) {
        binding.countVacancyTv.isVisible = true
        binding.countVacancyTv.setVacanciesCountText(state.content.found)
        binding.mainRv.isVisible = true
        binding.mainPb.isVisible = false
        binding.mainDefaultIv.isVisible = false
        binding.errorImageAndMessageLl.isVisible = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
