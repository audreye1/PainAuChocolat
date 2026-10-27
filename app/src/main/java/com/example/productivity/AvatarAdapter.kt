package com.example.productivity

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.productivity.AvatarDetailActivity.Companion.EXTRA_AVATAR
import androidx.appcompat.app.AppCompatActivity

class AvatarAdapter (var avatarList: List<Avatar>) :
    RecyclerView.Adapter<AvatarAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewDescription: TextView
        var imageViewAvatar: ImageView
        val layout: ConstraintLayout

        init {
            textViewName = view.findViewById(R.id.textView_avatar_name)
            textViewDescription = view.findViewById(R.id.textView_avatar_description)
            imageViewAvatar = view.findViewById(R.id.imageView_avatar_image)
            layout = view.findViewById(R.id.avatar_layout)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_avatar, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.textViewName.context
        // Get element from your dataset at this position and replace the contents of the view with that element
        var avatar = avatarList[position]
        viewHolder.textViewName.text = avatar.name
        viewHolder.textViewDescription.text = avatar.title
        var setimage = context.getResources().getIdentifier(avatar.image, "drawable", context.getPackageName())
        viewHolder.imageViewAvatar.setImageDrawable(context.getResources().getDrawable(setimage))

        //on click send the current avatar to the AvatarDetailActivity
        viewHolder.layout.setOnClickListener {
            val context = viewHolder.layout.context
            val detailIntent = Intent(context, AvatarDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_AVATAR, avatar)
            context.startActivity(detailIntent)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = avatarList.size
}