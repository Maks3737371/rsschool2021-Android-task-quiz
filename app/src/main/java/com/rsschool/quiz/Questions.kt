package com.rsschool.quiz

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentOnAttachListener
import java.util.zip.Inflater
import com.rsschool.quiz.databinding.FragmentQuizBinding

data class Question(val title: String, val options: List<String>, val trueAnswer: Int)

val questions = listOf(
    Question("Which number is greater?",    listOf("1", "0", "99", "64", "3"),                              3),
    Question("Which is green?",             listOf("Square", "Water", "Battery", "Grass", "Sun"),           4),
    Question("Who is faster?",              listOf("I`m", "Tortoise", "Panda", "Candy", "Light"),           5),
    Question("Lemon?",                      listOf("Lemon", "Not Lemon", "Some Lemon", "Banana", "Soup"),   1),
    Question("Have a nice day",             listOf("Thank you", "Thx, you too", "Meow", "0_0", "Ok"),       2))

val theme = listOf(
    R.style.Theme_Quiz_First,
    R.style.Theme_Quiz_Second,
    R.style.Theme_Quiz_Third,
    R.style.Theme_Quiz_Fourth,
    R.style.Theme_Quiz_Fifth)

val answer = MutableList<Int?>(questions.size) { null }


class QuestionFragment : Fragment(){
    private var _binding:FragmentQuizBinding? =null
    private lateinit var listener: TransitFragment
    private val binding get() = _binding!!


    override fun onAttach(context: Context){
        super.onAttach(context)
        listener = context as TransitFragment
        val questionNum = arguments?.getInt(QUESTION_NUM)?:0
        context.setTheme(theme[questionNum])
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val numberQuestion = arguments?.getInt(QUESTION_NUM)?:0
        val currentQuestion = questions[numberQuestion]
        binding.toolbar.title = "Question ${numberQuestion+1}"
        binding.question.text = currentQuestion.title
        binding.firstAnswer.text = currentQuestion.options[0]
        binding.secondAnswer.text = currentQuestion.options[1]
        binding.thirdAnswer.text = currentQuestion.options[2]
        binding.fourthAnswer.text = currentQuestion.options[3]
        binding.fifthAnswer.text = currentQuestion.options[4]

        if(answer[numberQuestion] != null)
        {
            when(answer[numberQuestion])
            {
                0 -> binding.firstAnswer.isChecked = true
                1 -> binding.secondAnswer.isChecked = true
                2 -> binding.thirdAnswer.isChecked = true
                3 -> binding.fourthAnswer.isChecked = true
                4 -> binding.fifthAnswer.isChecked = true
                else -> {}
            }
        }

        binding.nextButton.isEnabled = binding.radioGroup.checkedRadioButtonId != -1
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.first_answer) answer[numberQuestion] = 0
            if (checkedId == R.id.second_answer) answer[numberQuestion] = 1
            if (checkedId == R.id.third_answer) answer[numberQuestion] = 2
            if (checkedId == R.id.fourth_answer) answer[numberQuestion] = 3
            if (checkedId == R.id.fifth_answer) answer[numberQuestion] = 4
            binding.nextButton.isEnabled = true
        }


        if(numberQuestion == 0) binding.previousButton.isEnabled = false
        if(numberQuestion == (questions.size-1)) binding.nextButton.text = "SUBMIT"

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener{
            if (numberQuestion != 0) listener.TransitQuestion(numberQuestion-1)
        })

        binding.previousButton.setOnClickListener{
            listener.TransitQuestion(numberQuestion-1)
        }
        binding.nextButton.setOnClickListener{
            if (binding.nextButton.text =="SUBMIT")
            {
                var trueAnswerCounter = 0
                var result = ""
                var message = ""
                for(i in 0 until answer.size)
                {
                    if (answer[i] == questions[i].trueAnswer)
                        trueAnswerCounter++
                    message += "${i+1}) ${questions[i].title}\n You answer: ${questions[i].options[answer[i]?:0]}\n"
                }
                result = "Your result: $trueAnswerCounter of ${questions.size}"
                message = "$result\n\n $message"
                listener.TransitResult(result,message)
            }
            else
            {
                binding.nextButton.isEnabled = binding.radioGroup.checkedRadioButtonId != -1
                listener.TransitQuestion(numberQuestion + 1)
            }
        }
    }

    companion object{
        fun newInstance(questionNum: Int) : QuestionFragment{
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putInt(QUESTION_NUM, questionNum)
            fragment.arguments = args
            return fragment
        }
        const val QUESTION_NUM = "NUMBER"
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}