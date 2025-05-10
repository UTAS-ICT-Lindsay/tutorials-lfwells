package au.edu.utas.lfwells.week3app

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import au.edu.utas.lfwells.week3app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private lateinit var ui: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        //val myClickableButton = findViewById<Button>(R.id.myButton)
        val myClickableButton = ui.myButton;
        myClickableButton.setOnClickListener {
            myClickableButton.text = "Clicked!"
            Log.d("MY_TAG", "Button clicked!")
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}