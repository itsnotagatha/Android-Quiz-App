package com.example.quizapp

object Constants{

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS:String = "total_questions"
    const val CORRECT_ANSWERS:String = "correct_answers"

    //funkcja getQuestions, jedna stała zawierająca w sobie wszystkie pytania
    fun getQuestions(): ArrayList<Question>{
        //lista pytań jako arrayList
        val questionsList = ArrayList<Question>()
        //dodajemy pytanie do listy i zwracamy arraylist
        val que1 = Question(1,
            "What is the name of the highest mountain in Norway?",
            R.drawable.que1,
            "Galdhøpiggen",
            "Trolltunga",
            "Preikestolen",
            "Kjeragbolten",
            1
        )
        questionsList.add(que1)
        val que2 = Question(2,
            "Popular houses in Norway are called:",
            R.drawable.que2,
            "cabin",
            "hus",
            "telt",
            "hytte",
            4
        )
        questionsList.add(que2)
        val que3 = Question(3,
            "The most popular Norwegian inventions are:",
            R.drawable.que3,
            "Tetra Pak + zipper",
            "The cheese slicer + a paper clip",
            "wind Turbine + ice skates",
            "hot chocolate + the telescope",
            2
        )
        questionsList.add(que3)
        val que4 = Question(4,
            "What is the capital of Norway?",
            R.drawable.que4,
            "Bergen",
            "Oslo",
            "Drammen",
            "Stavanger",
            2
        )
        questionsList.add(que4)
        val que5 = Question(5,
            "Norway is the world's third largest exporter of:",
            R.drawable.que5,
            "oil",
            "computers",
            "motor vehicle parts",
            "salmon",
            1
        )
        questionsList.add(que5)
        val que6 = Question(6,
            "What year did the viking era end in Norway?",
            R.drawable.que6,
            "Battle of Hastings 793",
            "Battle of Paris 845",
            "Battle of Stiklestad in 1030",
            "Defeat of Harald HArdara in 1066",
            3
        )
        questionsList.add(que6)

        return  questionsList

    }
}