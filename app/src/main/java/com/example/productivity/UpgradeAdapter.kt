package com.example.productivity

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class UpgradeAdapter (var upgradeList: List<Upgrade>) :
    RecyclerView.Adapter<UpgradeAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewDescription: TextView
        var imageViewUpgrade: ImageView
        val layout: ConstraintLayout

        init {
            textViewName = view.findViewById(R.id.textView_upgrade_name)
            textViewDescription = view.findViewById(R.id.textView_upgrade_description)
            imageViewUpgrade = view.findViewById(R.id.imageView_upgrade_image)
            layout = view.findViewById(R.id.upgrade_layout)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_upgrade, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.textViewName.context
        var upgrade = upgradeList[position]
        viewHolder.textViewName.text = upgrade.name
        viewHolder.textViewDescription.text = upgrade.description
        Log.d("UpgradeAdapter", "${upgrade.image}")
        var setimage = context.getResources().getIdentifier(upgrade.image, "drawable", context.getPackageName())
        if (setimage != 0){
            viewHolder.imageViewUpgrade.setImageDrawable(context.getResources().getDrawable(setimage))
        }

        //on click send the current Upgrade to the UpgradeDetailActivity
        viewHolder.layout.setOnClickListener {
            val context = viewHolder.layout.context
            val detailIntent = Intent(context, UpgradeDetailActivity::class.java)
            detailIntent.putExtra(UpgradeDetailActivity.EXTRA_UPGRADE, upgrade)
            context.startActivity(detailIntent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = upgradeList.size

}