package com.example.classhangman

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HangmanModelView {
    var hangman: HangmanModel? = null
    val outside = Retrofit.Builder()
        .baseUrl("http://hangman.enti.cat:5002")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var hint: HintModel? = null
    fun getNewWord(hagmanTextOuput: TextView) {
        val request = outside.create(ApiHangman::class.java)
        request.getNewWord("cat").enqueue(object : Callback<HangmanModel> {
            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return
                hagmanTextOuput.text = hangman?.word?.replace("_","_ ") ?: ""
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
                hagmanTextOuput.text = "Error: $t"
            }
        })
    }
    fun getHint(context: Context){
        val request = outside.create(ApiHangman::class.java)
        request.getHint("${hangman?.token}").enqueue(object : Callback<HintModel> {
            override fun onResponse(call: Call<HintModel>, response: Response<HintModel>) {
                hint = response.body() ?: return
                Toast.makeText(context, "${hint?.hint}", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<HintModel>, t: Throwable) {
                Toast.makeText(context, "error HINT", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun guessLetter(letter: Char, hagmanTextOuput: TextView, context: Context) {
        val request = outside.create(ApiHangman::class.java)
        request.guessLetter(
            mapOf(
                "token" to hangman?.token,
                "letter" to letter.toString()
            )

        ).enqueue(object : Callback<HangmanModel> {

            override fun onResponse(call: Call<HangmanModel>, response: Response<HangmanModel>) {
                hangman = response.body() ?: return
                if (hangman?.correct == true)
                    hagmanTextOuput.text = hangman?.word?.replace("_","_ ") ?: ""
                else
                    Toast.makeText(context, "Letter $letter is incorrect", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<HangmanModel>, t: Throwable) {
                hagmanTextOuput.text = "Error: $t"
            }
        })
    }
    fun isGameWon(): Boolean {
        return false
    }
}