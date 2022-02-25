package com.feylabs.learnhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.feylabs.learnhilt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var someClass: SomeClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnClickMe.setOnClickListener {
            Toast.makeText(this, someClass.greet("Razky"), Toast.LENGTH_SHORT).show()
        }
    }
}

class SomeClass
@Inject
constructor(
    val otherClass: OtherClass
) {
    fun greet(name: String): String {
        return "Hello $name, you are ${otherClass.getWord()}"
    }
}

class OtherClass
@Inject
constructor() {
    fun getWord(): String = arrayOf("Beautiful", "Great", "Wonderful").random()
}