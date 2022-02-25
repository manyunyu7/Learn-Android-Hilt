package com.feylabs.learnhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.feylabs.learnhilt.databinding.ActivityMainBinding
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var someClass: SomeClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnClickMe.setOnClickListener {
//            Toast.makeText(this, someClass.greet("Razky"), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, someClass.sayGoodbye("Razky"), Toast.LENGTH_SHORT).show()
        }
    }
}

class RazUseInterface
constructor(
    razInterface: RazInterface
) {

}

class RazInterfaceImpl
@Inject
constructor() : RazInterface {
    override fun sayGoodBye(name: String): String {
        return "Goodbye $name"
    }

}

interface RazInterface {
    fun sayGoodBye(name: String): String
}

class SomeClass
@Inject
constructor(
    val otherClass: OtherClass,
    val razInterfaceImpl: RazInterfaceImpl
) {
    fun greet(name: String): String {
        return "Hello $name, you are ${otherClass.getWord()}"
    }

    fun sayGoodbye(name: String): String {
        return razInterfaceImpl.sayGoodBye(name)
    }
}

class OtherClass
@Inject
constructor() {
    fun getWord(): String = arrayOf("Beautiful", "Great", "Wonderful").random()
}

@InstallIn(ActivityComponent::class)
@Module
abstract class Modules {

    @Singleton
    @Binds
    abstract fun bindDependency(
        razInterfaceImpl: RazInterfaceImpl
    ): RazInterface
}