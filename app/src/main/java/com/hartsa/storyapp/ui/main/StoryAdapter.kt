package com.hartsa.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hartsa.storyapp.data.response.ListStoryItem
import com.hartsa.storyapp.databinding.ItemStoryBinding
import com.hartsa.storyapp.ui.detail.DetailStoryActivity

class StoryAdapter(private val listStory : List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = listStory.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }
    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = story.name
            binding.tvItemDescription.text = story.description
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                intentDetail.putExtra(DetailStoryActivity.STORY_ID, story.id)
                itemView.context.startActivity(intentDetail)
            }
        }
    }
}