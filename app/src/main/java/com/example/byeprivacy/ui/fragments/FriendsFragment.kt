package com.example.byeprivacy.ui.fragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentFriendsBinding
import com.example.byeprivacy.ui.viewmodels.BarsViewModel
import com.example.byeprivacy.ui.viewmodels.FriendsViewModel
import com.example.byeprivacy.ui.widgets.friends.AdapterFriends
import com.example.byeprivacy.ui.widgets.friends.RecyclerViewFollowers
import com.example.byeprivacy.utils.Injection

class FriendsFragment : Fragment() {
    private lateinit var binding: FragmentFriendsBinding
    private lateinit var viewModel: FriendsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
            FriendsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also { bind->

            bind.swiperefreshFollowers.setOnRefreshListener {
                viewModel.refreshDataFollowers()
                /*
                if (bind.refreshBanner.visibility == View.VISIBLE){
                    bind.refreshBanner.visibility = View.GONE
                }
                */
            }
            /*
            bind.follwersCounter.text=adapterFriends.getFollowersItemCount().toString()
            bind.followingCounter.text=adapterFriends.getFollowingItemCount().toString()
            */
            bind.addFriendButton.setOnClickListener{
                if(bind.addFriendText.text.isNotBlank()){
                    viewModel.addFriend(bind.addFriendText.text.toString())
                    bind.addFriendText.text.clear()
                }
            }

            viewModel.addedFriend.observe(viewLifecycleOwner){
                it?.getContentIfNotHandled()?.let {
                    if (it) {
                        viewModel.show("Friend added.")
                    }
                }
            }

            //TODO followers and functionality

            bind.followingCounter.setOnClickListener {
                bind.followingCounter.setTextColor(Color.parseColor("#533483"))
                bind.follwersCounter.setTextColor(Color.parseColor("#000000"))
                bind.followingCounter.textSize = 30.0F
                bind.follwersCounter.textSize = 15.0F
                bind.recyclerFollowing.visibility = View.VISIBLE
                bind.recyclerFollowrs.visibility = View.INVISIBLE
                viewModel.refreshDataFollowing()
            }
            bind.follwersCounter.setOnClickListener {
                bind.follwersCounter.setTextColor(Color.parseColor("#533483"))
                bind.followingCounter.setTextColor(Color.parseColor("#000000"))
                bind.follwersCounter.textSize = 30.0F
                bind.followingCounter.textSize = 15.0F
                bind.recyclerFollowing.visibility = View.INVISIBLE
                bind.recyclerFollowrs.visibility = View.VISIBLE
                viewModel.refreshDataFollowers()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.swiperefreshFollowers.isRefreshing = it
        }
        viewModel.message.observe(viewLifecycleOwner){
            if (PreferenceData.getInstance().getUserItem(requireContext()) == null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_global_loginFragment)
            }
        }
    }

}