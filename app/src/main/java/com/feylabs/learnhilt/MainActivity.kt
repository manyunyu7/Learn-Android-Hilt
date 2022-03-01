package com.feylabs.learnhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.feylabs.learnhilt.databinding.ActivityMainBinding
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject
import javax.inject.Qualifier
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
            Toast.makeText(this, someClass.greet("Razky"), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, someClass.saySomethingGood("Razky"), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, someClass.saySomethingGood("Razky"), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, someClass.saySomethingBad("Razky"), Toast.LENGTH_SHORT).show()
        }
    }
}

class RazInterfaceImpl
@Inject
constructor() : RazInterface {
    override fun sayGoodBye(name: String): String {
        return "Goodbye $name"
    }
}

class SaySomethingInterfaceImpl
@Inject
constructor(
    @StringGood val goodThings: String,
    @StringBad val badThings: String
) : SaySomethingInterface {

    override fun saySomethingGood(name: String): String {
        return "$goodThings $name"
    }

    override fun saySomethingBad(name: String): String {
        return "$badThings $name"
    }
}

interface RazInterface {
    fun sayGoodBye(name: String): String
}

interface SaySomethingInterface {
    fun saySomethingGood(name: String): String
    fun saySomethingBad(name: String): String
}

class SomeClass
@Inject
constructor(
    val otherClass: OtherClass,
    val razInterfaceImpl: RazInterface,
    val saySomethingInterfaceImpl: SaySomethingInterface
) {
    fun greet(name: String): String {
        return "Hello $name, you are ${otherClass.getWord()}"
    }

    fun sayGoodbye(name: String): String {
        return razInterfaceImpl.sayGoodBye(name)
    }

    fun saySomethingGood(name: String): String {
        return saySomethingInterfaceImpl.saySomethingGood(name)
    }

    fun saySomethingBad(name: String): String {
        return saySomethingInterfaceImpl.saySomethingBad(name)
    }

}

class OtherClass
@Inject
constructor() {
    fun getWord(): String = arrayOf("Beautiful", "Great", "Wonderful").random()
}

/* This is Exammple Using Module and abstact class */
//@InstallIn(ActivityComponent::class)
//@Module
//abstract class Modules {
//
//    @Singleton
//    @Binds
//    abstract fun bindDependency(
//        razInterfaceImpl: RazInterfaceImpl
//    ): RazInterface
//}


/* This is Example Using Module and much easier to use injection with provides */
@InstallIn(ActivityComponent::class)
@Module
class ModulesMuchEasier {

    @StringGood
    @Provides
    fun provideGoodWords(): String {
        return arrayOf("Wonderful").random()
    }

    @Provides
    fun provideGoodThingsInterface(): SaySomethingInterface {
        return SaySomethingInterfaceImpl(provideGoodWords(), provideBadWord())
    }

    @Provides
    fun provideDependency(): RazInterface {
        return RazInterfaceImpl()
    }

    @StringBad
    @Provides
    fun provideBadWord(): String {
        return "Bad"
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StringBad

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StringGood

