package com.example.quizapp

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*


class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    //FINGERPRINT
    private var cancellationSignal: CancellationSignal? = null
    //info about trying to log by fingerprint
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get()=
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(this@QuizQuestionsActivity, ResultActivity::class.java)
                    //fill it with our name and score
                    intent.putExtra(Constants.USER_NAME, mUserName)
                    intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                    intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                    startActivity(intent)
                    finish()
                    notifyUser("Authentication success!")
//                    startActivity(Intent(this@QuizQuestionsActivity, ResultActivity::class.java))
                }
            }
    @RequiresApi(Build.VERSION_CODES.P)

    //which question we currently are at and then which option is selected
    //we always start with the que nr 1
    private var mCurrentPositon:Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition:Int = 0
    private var mUserName: String? = null

    //app need to know how many times we answered correctly
    //assign it each time assign the green btn
    private var mCorrectAnswers: Int = 0

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        //FINGERPRINT
        checkBiometricSupport()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        //get our name
        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        //added View.OnClickListener (line 13) to activate our quizQuestionActivity
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four .setOnClickListener(this)

        //give our button "submit" listener
        btn_submit.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setQuestion(){
        //model that we created in Question.kt is used here over and over again

        //pytanie, które aktualnie widzimy będzie typu Question (o dopuszczalnej wartości pustej
        //coś, co przypisaliśmy do listy pytań na obecnym stanowisku - 1
        //-1 bo chcemy mieć index = 0
        val question = mQuestionsList!![mCurrentPositon -1]

        //all the options are back to default style
        defaultOptionsView()
        if(mCurrentPositon == mQuestionsList!!.size){
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPositon
        tv_progress.text = "$mCurrentPositon" + "/" + progressBar.max

        //ustawiamy możliwe 4 opcje do wyboru, do każdego nowe opcje wyboru
        tv_question.text = question!!.question
        tv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        //default options style
        for(option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            //bold text
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this,R.drawable.default_option_border_bg)
        }
    }

    //what to do when user clicks on the button (selected option)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onClick(v: View?) {
        //after choosing one option function selectedOptionView goes through his code
        when(v?.id){
            R.id.tv_option_one ->{
                selectedOptionView(tv_option_one, 1)
            }
            R.id.tv_option_two ->{
                selectedOptionView(tv_option_two, 2)
            }
            R.id.tv_option_three ->{
                selectedOptionView(tv_option_three, 3)
            }
            R.id.tv_option_four ->{
                selectedOptionView(tv_option_four, 4)
            }
            //what shall happen after clicking "submit" (go to the next question)
            R.id.btn_submit ->{
                if(mSelectedOptionPosition == 0){
                    mCurrentPositon++

                    when{
                        mCurrentPositon <= mQuestionsList!!.size ->{
                            //reset the next question
                            setQuestion()
                        }else ->{
                        //move to result page
                        val biometricPrompt = BiometricPrompt.Builder(this)
                            .setTitle("Authentication")
                            .setSubtitle("Authentication is required")
                            .setDescription("This app uses fingerprint protection to keep your data secure")
                            .setNegativeButton("Cancel", this.mainExecutor,
                                DialogInterface.OnClickListener{ dialog, which ->
                                    notifyUser("Authentication cancelled")
                                }).build()

                        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)


                        }
                    }
                }else{
                    //nothing was chosen
                    val question = mQuestionsList?.get(mCurrentPositon - 1)
                    //checking if the answer is correct
                    if(question!!.correctAnswer !=mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        //adding point
                        mCorrectAnswers++
                    }

                    answerView(question.correctAnswer,R.drawable.correct_option_border_bg)
                    //changing the text of our btn
                    if(mCurrentPositon == mQuestionsList!!.size){
                        btn_submit.text = "FINISH"
                    }else{
                        btn_submit.text = "GO TO NEXT QUESTION"
                    }

                    //we need to set our position to 0 again to go to the next question
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    //assign the right color for the background to out options
    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 ->{
                tv_option_one.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 ->{
                tv_option_two.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 ->{
                tv_option_three.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 ->{
                tv_option_four.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
        //reset to the default view
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber

        //our new styles for selected option
        tv.setTextColor(Color.parseColor("#789077"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this,R.drawable.selected_option_border_bg)
    }


    //FINGERPRINT
    private fun notifyUser(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal{
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }
    private fun checkBiometricSupport():Boolean{
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if(!keyguardManager.isKeyguardSecure){
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }
        if(ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.USE_BIOMETRIC)!= PackageManager.PERMISSION_GRANTED){
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true
    }
}