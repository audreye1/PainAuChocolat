package com.example.productivity

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault

class ToDoAdapter (var toDoList: MutableList<ToDo>):
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewLength: TextView
        val layout: ConstraintLayout

        init {
            textViewName = view.findViewById(R.id.textView_toDoList_name)
            textViewLength = view.findViewById(R.id.textView_toDoList_length)
            layout = view.findViewById(R.id.todo_layout)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var toDo = toDoList[position]
        val context = holder.textViewName.context
        holder.textViewName.text = toDo.name
        var text = ""
        if(ToDo.EMOJI.valueOf(toDo.length).emoji.contains("Long")){
            text = ToDo.EMOJI.valueOf(toDo.length).emoji.replace("Long ", "")
        }
        else if(ToDo.EMOJI.valueOf(toDo.length).emoji.contains("Medium")){
            text = ToDo.EMOJI.valueOf(toDo.length).emoji.replace("Medium ", "")
        }
        else if(ToDo.EMOJI.valueOf(toDo.length).emoji.contains("Short")){
            text = ToDo.EMOJI.valueOf(toDo.length).emoji.replace("Short ", "")
        }
        holder.textViewLength.text = text
        holder.layout.isLongClickable = true
        holder.layout.setOnLongClickListener {
            val popMenu = PopupMenu(context, holder.textViewName)
            popMenu.inflate(R.menu.menu_todo_list_context)
            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_todo_delete -> {
                        deleteFromBackendless(position)
                        true
                    }

                    else -> true
                }
            }
            popMenu.show()
            true

        }

        //on click send the current to do to its detail activity
        holder.layout.setOnClickListener {
            val context = holder.layout.context
            val detailIntent = Intent(context, ToDoDetailActivity::class.java)
            detailIntent.putExtra(ToDoDetailActivity.EXTRA_TODO, toDo)
            context.startActivity(detailIntent)
        }
    }

    private fun deleteFromBackendless(position: Int) {
        Log.d("ToDoAdapter", "deleteFromBackendless: Trying to delete ${toDoList[position]}")
        Backendless.Data.of(ToDo::class.java).save(toDoList[position], object : AsyncCallback<ToDo> {
            override fun handleResponse(savedToDoEntry: ToDo) {
                Backendless.Data.of(ToDo::class.java).remove(savedToDoEntry, object : AsyncCallback<Long> {
                    override fun handleResponse(response: Long) {
                        toDoList.remove(toDoList[position])
                        notifyDataSetChanged()
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // an error has occurred, the error code can be retrieved with fault.code
                    }
                })
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.code
            }
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = toDoList.size
}