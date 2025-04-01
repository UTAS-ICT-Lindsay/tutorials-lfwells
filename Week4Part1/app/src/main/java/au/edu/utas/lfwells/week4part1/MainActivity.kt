package au.edu.utas.lfwells.week4part1

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import au.edu.utas.lfwells.week4part1.databinding.ActivityMainBinding
import au.edu.utas.lfwells.week4part1.databinding.DumbLayoutBinding
import au.edu.utas.lfwells.week4part1.databinding.MyListItemBinding

class Person (
    var name : String,
    var studentID: Int,
    var smort: Boolean
)

val items = mutableListOf(
    Person(name = "Rick", studentID = 9001, smort = true),
    Person(name = "Morty", studentID = 9, smort = true),
    Person(name = "Beth", studentID = 42, smort = true),
    Person(name = "Summer", studentID = 43, smort = true),
    Person(name = "Dongyi", studentID = -1, smort = false)
)

class MainActivity : AppCompatActivity()
{
    private lateinit var ui : DumbLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ui = DumbLayoutBinding.inflate(layoutInflater)
        setContentView(ui.root)

        ViewCompat.setOnApplyWindowInsetsListener(ui.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ui.myList.adapter = PersonAdapter(people = items)
        ui.myList.layoutManager = LinearLayoutManager(this)
    }

    inner class PersonHolder(var ui: MyListItemBinding) : RecyclerView.ViewHolder(ui.root) {}
    inner class PersonAdapter(private val people: MutableList<Person>) : RecyclerView.Adapter<PersonHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonHolder {
            Log.d("PersonAdapter", "onCreateViewHolder")
            //the ui list item gets created here
            val ui = MyListItemBinding.inflate(layoutInflater, parent, false)
            return PersonHolder(ui)
        }

        override fun getItemCount(): Int {
            return people.size
        }

        override fun onBindViewHolder(holder: PersonHolder, position: Int) {
            Log.d("PersonAdapter", "onBindViewHolder")
            //and then it gets used here
            val thisPerson = people[position]
            holder.ui.txtName.text = thisPerson.name
            holder.ui.txtStudentID.text = thisPerson.studentID.toString()

            holder.ui.imageButton2.setOnClickListener {
                items.removeAt(position)
                //people.removeAt(position)
                //notifyDataSetChanged()
                notifyItemRemoved(position)
            }

            holder.itemView.setOnClickListener {
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle(thisPerson.name)
                /*if (thisPerson.smort) {
                    builder.setMessage("This person is smort")
                } else {
                    builder.setMessage("This person is not smort")
                }*/
                builder.setMessage(if (thisPerson.smort) "they are smart" else "they are not smart")
                //(thisPerson.smort) ? "they are smart" : "they are not smart"
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }

                builder.setNeutralButton("Maybe") { dialog, which ->
                    Toast.makeText(applicationContext,
                        "Maybe", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }


        }
    }
}