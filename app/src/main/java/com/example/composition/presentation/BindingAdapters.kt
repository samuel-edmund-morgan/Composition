package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int){
            textView.text = String.format(
            textView.context.getString(R.string.score_answers),
            count
        )
}


@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percent: Int){
    textView.text = String.format(
            textView.context.getString(R.string.required_percentage),
            percent
        )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        textView.context.getString(R.string.score_percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

private fun getPercentOfRightAnswers(gameResult: GameResult) : Int{
    return if (gameResult.countOfQuestions == 0)
        0
    else
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
}

@BindingAdapter("setImage")
fun bindSetImage(imageView: ImageView, gameResult: GameResult){
    imageView.setImageResource(getSmileResId(gameResult))
}


private fun getSmileResId(gameResult: GameResult): Int{
    return if(gameResult.winner){
        R.drawable.ic_smile
    }else{
        R.drawable.ic_sad
    }
}

