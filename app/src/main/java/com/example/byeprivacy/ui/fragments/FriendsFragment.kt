package com.example.byeprivacy.ui.fragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentBarsBinding
import com.example.byeprivacy.databinding.FragmentFriendsBinding
import com.example.byeprivacy.ui.viewmodels.FriendsViewModel
import com.example.byeprivacy.ui.widgets.friends.list.AdapterFollowing
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
        Log.d("friends on binding","friends on binding")

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel

        }.also { bind->


            bind.swiperefreshFollowers.setOnRefreshListener {
                viewModel.refreshDataFollowing()
                //viewModel.refreshDataFollowers()

            }
            bind.addFriendButton.setOnClickListener{
                if(bind.addFriendText.text.isNotBlank()){
                    viewModel.addFriend(bind.addFriendText.text.toString())
                    bind.addFriendText.text.clear()
                    /*if (bind.recyclerFollowrs.visibility == View.VISIBLE){
                        //viewModel.refreshDataFollowers()
                    }*/
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

            bind.following.setOnClickListener {
                bind.following.setTextColor(Color.parseColor("#533483"))
                //bind.followers.setTextColor(Color.parseColor("#000000"))
                bind.following.textSize = 30.0F
               // bind.followers.textSize = 15.0F
                bind.recyclerFollowing.visibility = View.VISIBLE
                //bind.recyclerFollowrs.visibility = View.INVISIBLE
                viewModel.refreshDataFollowing()
            }
            /*
            bind.followers.setOnClickListener {
                bind.followers.setTextColor(Color.parseColor("#533483"))
                bind.following.setTextColor(Color.parseColor("#000000"))
                bind.followers.textSize = 30.0F
                bind.following.textSize = 15.0F
                bind.recyclerFollowing.visibility = View.INVISIBLE
                //bind.recyclerFollowrs.visibility = View.VISIBLE
                //viewModel.refreshDataFollowers()
            }

             */
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