package com.alfa.githubuserapp.ui.detailuser.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alfa.githubuserapp.data.source.remote.responses.DetailUserResponse
import com.alfa.githubuserapp.databinding.FragmentProfileBinding
import com.alfa.githubuserapp.ui.detailuser.DetailUserActivity
import com.alfa.githubuserapp.ui.detailuser.DetailUserViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var detailUser: DetailUserResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        val bundle = Bundle()
        val username =
            requireActivity().intent.getStringExtra(DetailUserActivity.EXTRA_USERNAME).toString()
        val userId = requireActivity().intent.getIntExtra(DetailUserActivity.EXTRA_ID, 0)

        binding.apply {

            viewModel = ViewModelProvider(requireActivity())[DetailUserViewModel::class.java]

            viewModel.setUserDetail(username)

            viewModel.isLoading.observe(requireActivity()) {
                showLoading(it)
            }
            viewModel.onFailure.observe(requireActivity()) {
                onFailure(it)
            }
            viewModel.getUserDetail().observe(requireActivity()) {
                if (it != null) {
                    detailUser = it

                    bundle.putInt(DetailUserActivity.EXTRA_FOLLOWERS, it.followers)
                    bundle.putInt(DetailUserActivity.EXTRA_FOLLOWING, it.following)

                    binding.apply {
                        tvDetailName.text = it.name
                        tvDetailUsername.text = it.login
                        tvDetailFollowers.text = it.followers.toString()
                        tvDetailFollowing.text = it.following.toString()
                        tvDetailRepository.text = it.publicRepos.toString()
                        tvDetailCompany.text = it.company ?: "-"
                        tvDetailLocation.text = it.location ?: "-"

                        Glide.with(this@ProfileFragment)
                            .load(it.avatarUrl)
                            .centerCrop()
                            .into(ivDetailProfile)

                    }
                }
            }
        }

        binding.apply {
            var isChecked = false
            CoroutineScope(Dispatchers.IO).launch {
                val count = viewModel.checkUser(userId)
                withContext(Dispatchers.Main) {
                    if (count != null) {
                        if (count > 0) {
                            toggleFavorite.isChecked = true
                            isChecked = true
                        } else {
                            toggleFavorite.isChecked = false
                            isChecked = false
                        }
                    }
                }
            }

            toggleFavorite.setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    viewModel.addToFavorite(
                        username,
                        userId,
                        detailUser.avatarUrl,
                        detailUser.htmlUrl
                    )
                    Toast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.removeFromFavorite(userId)
                    Toast.makeText(requireContext(), "Removed from favorite", Toast.LENGTH_SHORT)
                        .show()
                }
                toggleFavorite.isChecked = isChecked
            }




            btnRefresh.setOnClickListener {
                viewModel.setUserDetail(username)
            }

            btnShare.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "$username on CodeHub: ${detailUser.htmlUrl}")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun onFailure(fail: Boolean) {
        binding.apply {
            if (fail) {
                tvOnFailMsg.visibility = View.VISIBLE
            } else {
                tvOnFailMsg.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}