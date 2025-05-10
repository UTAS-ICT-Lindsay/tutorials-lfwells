package au.edu.utas.lfwells.week3livedemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import au.edu.utas.lfwells.week3livedemo.databinding.ActivityMainBinding

const val USERNAME_KEY = "username"

class MainActivity : AppCompatActivity()
{
    private lateinit var ui : ActivityMainBinding

    private val getCharacterHandler = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //in here we will check
            //result.resultCode
            //and use
            //result.data
            //see later code
            Log.d("MainActivity", "Returned from SecondScreen")

            if (result.resultCode == RESPONSE_CANCELLED)
            {
                //
                ui.lblCharacter.text = "You cancelled, you monster!"
            }
            else if (result.resultCode == RESPONSE_SELECTED)
            {
                // Get the name from the intent and display it in the text view
                val character = result.data!!.getStringExtra(CHARACTER_NAME)
                ui.lblCharacter.text = character
            }

    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ui.btnSelect.setOnClickListener {
            val intent = Intent(this, SecondScreen::class.java) //this line is the same as the tutorials
            getCharacterHandler.launch(intent)
            //not doing startActivity(intent)
        }


    }
}