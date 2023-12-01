package com.alfa.githubuserapp.ui.detailuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alfa.githubuserapp.data.model.UserEntity
import com.alfa.githubuserapp.databinding.ItemFollowerBinding
import com.bumptech.glide.Glide

class UserFollowsAdapter : RecyclerView.Adapter<UserFollowsAdapter.UserViewHolder>() {
    private val list = ArrayList<UserEntity>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setList(users: List<UserEntity>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemFollowerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(ivItemProfile)
                tvItemUsername.text = user.username
                tvItemUserUrl.text = user.htmlUrl

                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemFollowerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {

        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }
}