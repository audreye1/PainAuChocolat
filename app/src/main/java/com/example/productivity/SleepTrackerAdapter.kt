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

class SleepTrackerAdapter (var sleepTrackerList: MutableList<SleepTracker>):
    RecyclerView.Adapter<SleepTrackerAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDate: TextView
        val textViewTime: TextView
        val layout: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View
            textViewDate = view.findViewById(R.id.textView_sleepTracker_date)
            textViewTime = view.findViewById(R.id.textView_sleepTracker_time)
            layout = view.findViewById(R.id.sleepTracker_layout)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sleep_tracker, parent, false)
        val holder = ViewHolder(view)
        return holder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var sleepTracker = sleepTrackerList[position]
        val context = holder.textViewDate.context
        holder.textViewDate.text = sleepTracker.date
        holder.textViewTime.text = sleepTracker.time

        holder.layout.isLongClickable = true
        holder.layout.setOnLongClickListener {
            val popMenu = PopupMenu(context, holder.textViewDate)
            popMenu.inflate(R.menu.menu_sleeptracker_list_context)
            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_sleeptracker_delete -> {
                        deleteFromBackendless(position)
                        true
                    }
                    else -> true
                }
            }
            popMenu.show()
            true
        }

        //on click send the current SleepTracker to SleepTrackerDetailActivity
        holder.layout.setOnClickListener {
            val context = holder.layout.context
            val detailIntent = Intent(context, SleepTrackerDetailActivity::class.java)
            detailIntent.putExtra(SleepTrackerDetailActivity.EXTRA_SLEEPTRACKER, sleepTracker)
            context.startActivity(detailIntent)
        }
    }

    private fun deleteFromBackendless(position: Int) {
        Log.d("SleepTrackerAdapter", "deleteFromBackendless: Trying to delete ${sleepTrackerList[position]}")
        Backendless.Data.of(SleepTracker::class.java).save(sleepTrackerList[position], object : AsyncCallback<SleepTracker> {
            override fun handleResponse(savedSleepTrackerEntry: SleepTracker) {
                Backendless.Data.of(SleepTracker::class.java).remove(savedSleepTrackerEntry, object : AsyncCallback<Long> {
                    override fun handleResponse(response: Long) {
                        sleepTrackerList.remove(sleepTrackerList[position])
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
    override fun getItemCount() = sleepTrackerList.size

}