package au.edu.utas.kit305.tutorial05

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit305.tutorial05.databinding.ActivityMainBinding
import au.edu.utas.kit305.tutorial05.databinding.ActivityMovieDetailsBinding
import au.edu.utas.kit305.tutorial05.databinding.MyListItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

val items = mutableListOf<Movie>()

const val FIREBASE_TAG = "FirebaseLogging"
const val MOVIE_INDEX = "Movie_Index"

class MainActivity : AppCompatActivity()
{
    private lateinit var ui : ActivityMainBinding

    override fun onResume() {
        super.onResume()

        ui.myList.adapter?.notifyDataSetChanged() //without a more complicated set-up, we can't be more specific than "dataset changed"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ui.lblMovieCount.text = "${items.size} Movies"
        ui.myList.adapter = MovieAdapter(movies = items)

        //vertical list
        ui.myList.layoutManager = LinearLayoutManager(this)

        //get db connection
        val db = Firebase.firestore
        Log.d("FIREBASE", "Firebase connected: ${db.app.name}")

        //add some data (comment this out after running the program once and confirming your data is there)
        val lotr = Movie(
            title = "Lord of the Rings: Fellowship of the Ring",
            year = 2001,
            duration = 9001F
        )

        val moviesCollectionRef = db.collection("movies")

        /*
        moviesCollectionRef
            .add(lotr)
                .addOnSuccessListener { doc ->
                    Log.d(FIREBASE_TAG, "Document created with id ${doc.id}")
                    lotr.id = doc.id
                }
                .addOnFailureListener {
                    Log.e(FIREBASE_TAG, "Error writing document", it)
                }*/

        ui.lblMovieCount.text = "Loading..."
        moviesCollectionRef
            //.whereGreaterThan("year", 2000)
            .get()
            .addOnSuccessListener { result ->
                items.clear() //this line clears the list, and prevents a bug where items would be duplicated upon rotation of screen
                Log.d(FIREBASE_TAG, "--- all movies ---")
                for (document in result)
                {
                    Log.d(FIREBASE_TAG, document.toString())
                    val movie = document.toObject<Movie>()
                    movie.id = document.id
                    Log.d(FIREBASE_TAG, movie.toString())

                    items.add(movie)
                }

                ui.lblMovieCount.text = "Movie Count: ${items.size}"
                (ui.myList.adapter as MovieAdapter).notifyDataSetChanged()
                //you may choose to fix the warning that notifyDataSetChanged() is not specific enough using:
                //(ui.myList.adapter as MovieAdapter).notifyItemRangeInserted(0, items.size)
            }

        //the interesting way, using an AlertDialog
        val dialogBuilder = android.app.AlertDialog.Builder(this)
        val dialogUI = ActivityMovieDetailsBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogUI.root)

        ui.btnAdd.setOnClickListener {
            //the easy way, reusing the whole MovieDetails class
            /*val i = Intent(this, MovieDetails::class.java)
            startActivity(i)*/

            val dialog = dialogBuilder.show()
            dialogUI.btnSave.setOnClickListener {
                //get the user input
                val movieObject = Movie(
                    title = dialogUI.txtTitle.text.toString(),
                    year = dialogUI.txtYear.text.toString().toInt(), //good code would check this is really an int
                    duration = dialogUI.txtDuration.text.toString().toFloat() //good code would check this is really a float
                )

                //add the movie to the database
                moviesCollectionRef
                    .add(movieObject)
                    .addOnSuccessListener { doc ->
                        Log.d(FIREBASE_TAG, "Document created with id ${doc.id}")
                        movieObject.id = doc.id
                        items.add(movieObject)
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Log.e(FIREBASE_TAG, "Error writing document", it)
                    }
            }
        }
    }

    inner class MovieHolder(var ui: MyListItemBinding) : RecyclerView.ViewHolder(ui.root) {}

    inner class MovieAdapter(private val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivity.MovieHolder {
            val ui = MyListItemBinding.inflate(layoutInflater, parent, false)   //inflate a new row from the my_list_item.xml
            return MovieHolder(ui)                                                            //wrap it in a ViewHolder
        }

        override fun getItemCount(): Int {
            return movies.size
        }

        override fun onBindViewHolder(holder: MainActivity.MovieHolder, position: Int) {
            val movie = movies[position]   //get the data at the requested position
            holder.ui.txtName.text = movie.title
            holder.ui.txtYear.text = movie.year.toString()

            holder.ui.root.setOnClickListener {
                val i = Intent(holder.ui.root.context, MovieDetails::class.java)
                i.putExtra(MOVIE_INDEX, position)
                startActivity(i)
            }
        }
    }
}

