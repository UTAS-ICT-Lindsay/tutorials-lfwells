package au.edu.utas.kit305.tutorial05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import au.edu.utas.kit305.tutorial05.databinding.ActivityMovieDetailsBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MovieDetails : AppCompatActivity() {
    private lateinit var ui : ActivityMovieDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(ui.root)


        //spinner stuff
        ui.mySpinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.movie_directors,
            android.R.layout.simple_spinner_item
        )
        ui.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // An item was selected. You can retrieve the selected item using
                Log.d("SPINNER", adapter?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }


        val db = Firebase.firestore
        val moviesCollection = db.collection("movies")

        //read in movie details and display on this screen
        //get movie object using id from intent
        val movieID = intent.getIntExtra(MOVIE_INDEX, -1)

        val editingMode = movieID != -1

        if (editingMode) {
            val movieObject = items[movieID]

            //set txtTitle, txtYear, txtDuration yourself
            ui.txtTitle.setText(movieObject.title)
            ui.txtYear.setText(movieObject.year.toString())
            ui.txtDuration.setText(movieObject.duration.toString())

            ui.btnSave.setOnClickListener {
                //get the user input
                movieObject.title = ui.txtTitle.text.toString()
                movieObject.year = ui.txtYear.text.toString().toInt() //good code would check this is really an int
                movieObject.duration = ui.txtDuration.text.toString().toFloat() //good code would check this is really a float

                //update the database need a document reference
                moviesCollection.document(movieObject.id!!)
                    .set(movieObject, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d(FIREBASE_TAG, "Successfully updated movie ${movieObject.id}")
                        //return to the list
                        finish()
                    }
                    .addOnFailureListener {
                        Log.w(FIREBASE_TAG, "Error updating document", it)
                    }
            }
        }
        else
        {
            ui.btnSave.setOnClickListener {
                //get the user input
                val movieObject = Movie(
                    title = ui.txtTitle.text.toString(),
                    year = ui.txtYear.text.toString().toInt(), //good code would check this is really an int
                    duration = ui.txtDuration.text.toString().toFloat() //good code would check this is really a float
                )

                //add the movie to the database
                moviesCollection
                    .add(movieObject)
                    .addOnSuccessListener { doc ->
                        Log.d(FIREBASE_TAG, "Document created with id ${doc.id}")
                        movieObject.id = doc.id
                        items.add(movieObject)
                        finish()
                    }
                    .addOnFailureListener {
                        Log.e(FIREBASE_TAG, "Error writing document", it)
                    }
            }
        }



    }
}