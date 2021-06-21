package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.ResultFragmentBinding
import kotlin.system.exitProcess

class ResultFragment : Fragment(){
    private var _binding: ResultFragmentBinding? = null
    private lateinit var listener: TransitFragment
    private val binding get() = _binding!!

    override fun onAttach(context: Context){
        super.onAttach(context)
        listener = context as TransitFragment
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = ResultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(RESULT).also{
            binding.resultTextView.text = it
        }
        val message = arguments?.getString(MESSAGE)
        binding.repeatImage.setOnClickListener{
            for(i in 0 until answer.size){
                answer[i] = null
            }
            listener.TransitQuestion(0)
        }
        binding.exitImage.setOnClickListener{
            exitProcess(0)

        }
        binding.shareImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz Result")
            intent.putExtra(Intent.EXTRA_TEXT,message)
            startActivity(intent)
        }
    }


    companion object{

        fun newInstance(result: String, message: String) : ResultFragment{
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(RESULT, result)
            args.putString(MESSAGE,message)
            fragment.arguments = args
            return fragment
        }

        private const val RESULT = "TRUE_ANSWER_FROM_USER"
        private const val MESSAGE = "MESSAGE_TO_SEND"
    }
    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }
}
