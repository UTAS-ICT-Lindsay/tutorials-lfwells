package au.edu.utas.kit305.tutorial05

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.kit305.tutorial05.databinding.ActivityMainBinding
import au.edu.utas.kit305.tutorial05.databinding.ActivityMovieDetailsBinding
import au.edu.utas.kit305.tutorial05.databinding.MyListItemBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

val items = mutableListOf<Movie>()

const val FIREBASE_TAG = "FirebaseLogging"
const val MOVIE_INDEX = "Movie_Index"

class MainActivity : AppCompatActivity()
{
    private lateinit var ui : ActivityMainBinding

    private lateinit var moviesCollectionRef : CollectionReference

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

        //set up swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = ui.myList.adapter as MovieAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(ui.myList)

        //get db connection
        val db = Firebase.firestore
        Log.d("FIREBASE", "Firebase connected: ${db.app.name}")

        //add some data (comment this out after running the program once and confirming your data is there)
        val lotr = Movie(
            title = "Lord of the Rings: Fellowship of the Ring",
            year = 2001,
            duration = 9001F
        )

        moviesCollectionRef = db.collection("movies")

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

        fun removeAt(position: Int) {

            //delete from the databaes
            moviesCollectionRef.document(movies[position].id!!)
                .delete()
                .addOnSuccessListener {
                    Log.d(FIREBASE_TAG, "DocumentSnapshot successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.w(FIREBASE_TAG, "Error deleting document", e)
                }

            items.removeAt(position)
            notifyItemRemoved(position)

            ui.lblMovieCount.text = "Movie Count: ${items.size}"
        }
    }
}
//https://github.com/kitek/android-rv-swipe-delete/blob/master/app/src/main/java/pl/kitek/rvswipetodelete/SwipeToDeleteCallback.kt
abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, android.R.drawable.ic_delete)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }



    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        // Calculate position of delete icon
        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight!!) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight

        // Draw the delete icon
        deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon?.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        /**
         * To disable "swipe" for specific item return 0 here.
         * For example:
         * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
         * if (viewHolder?.adapterPosition == 0) return 0
         */
        if (viewHolder?.adapterPosition == 10) return 0
        return super.getMovementFlags(recyclerView, viewHolder)
    }
}