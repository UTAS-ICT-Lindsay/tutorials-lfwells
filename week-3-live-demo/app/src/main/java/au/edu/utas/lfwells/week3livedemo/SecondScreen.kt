package au.edu.utas.lfwells.week3livedemo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import au.edu.utas.lfwells.week3livedemo.databinding.ActivitySecondScreenBinding

const val RESPONSE_CANCELLED = 0
const val RESPONSE_SELECTED = 1
const val CHARACTER_NAME = "NAME"

class SecondScreen : AppCompatActivity()
{
    private lateinit var ui : ActivitySecondScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ui = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get the name from the intent and display it in the text view
        //val name = intent.getStringExtra(USERNAME_KEY)
        //ui.lblEnteredText.text = name

        ui.btnCancel.setOnClickListener {
            setResult(RESPONSE_CANCELLED)
            finish()
        }

        ui.btnChar.setOnClickListener {
            val intent = Intent()
            intent.putExtra(CHARACTER_NAME, "charmander")
            setResult(RESPONSE_SELECTED, intent)

            finish()
        }

        ui.btnBulb.setOnClickListener {
            //lindsay made it so this button would crash
            //val intent = Intent()
            //intent.putExtra(CHARACTER_NAME, "bulbasaur")
            //setResult(RESPONSE_SELECTED, intent)
            setResult(RESPONSE_SELECTED)

            finish()
        }

        ui.btnSquirtle.setOnClickListener {
            val intent = Intent()
            intent.putExtra(CHARACTER_NAME, "squirtle")
            setResult(RESPONSE_SELECTED, intent)

            finish()
        }
    }
}