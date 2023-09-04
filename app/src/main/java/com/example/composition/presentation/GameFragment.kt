package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
        add(binding.tvOption1)
        add(binding.tvOption2)
        add(binding.tvOption3)
        add(binding.tvOption4)
        add(binding.tvOption5)
        add(binding.tvOption6)
        }
    }

    private lateinit var level: Level
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setClickListenersToOptions()
        gameViewModel.startGame(level)

    }

    private fun setClickListenersToOptions(){
        for (tvOption in tvOptions){
            tvOption.setOnClickListener {
                gameViewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeViewModel(){
        gameViewModel.question.observe(viewLifecycleOwner){
            binding.tvSum.text = it.sum.toString()
            binding.tvLeftNumber.text = it.visibleNumber.toString()
            for(i in 0 until tvOptions.size){
                tvOptions[i].text = it.options[i].toString()
            }
        }
        gameViewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            binding.progressBar.setProgress(it, true)
        }
        gameViewModel.enoughCount.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.setTextColor(getColorByState(it))
        }
        gameViewModel.enoughPercent.observe(viewLifecycleOwner){
            val color = getColorByState(it)
            //Встановлення кольору у прогресбару
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        gameViewModel.formattedTime.observe(viewLifecycleOwner){
            binding.tvTimer.text = it
        }
        gameViewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
        gameViewModel.gameResult.observe(viewLifecycleOwner){
            launchGameFinishedFragment(it)
        }
        gameViewModel.progressAnswers.observe(viewLifecycleOwner){
            binding.tvAnswersProgress.text = it
        }

    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs(){
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let{
            level = it
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, GameFinishedFragment.newInstance(gameResult))
            .commit()
    }

    companion object{
        private const val  KEY_LEVEL = "level"
        const val NAME = "GameFragment"
        fun newInstance(level: Level):GameFragment{
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}