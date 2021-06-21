package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import androidx.annotation.TransitionRes
import com.rsschool.quiz.QuestionFragment.Companion.newInstance

class MainActivity : AppCompatActivity(), TransitFragment{
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TransitQuestion(0)
    }

    override fun TransitQuestion(question: Int){
        val fragment = newInstance(question)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun TransitResult(result: String, message: String){
        val fragment = ResultFragment.newInstance(result, message)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}