package com.example.byeprivacy.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentBarsBinding
import com.example.byeprivacy.ui.viewmodels.BarsViewModel
import com.example.byeprivacy.ui.viewmodels.LoginViewModel
import com.example.byeprivacy.utils.Injection

class BarsFragment : Fragment() {
    private lateinit var binding: FragmentBarsBinding
    private lateinit var viewModel: BarsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
            BarsViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }.also {
            bind->
            bind.swiperefresh.setOnRefreshListener {
                viewModel.refreshData()
            }
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.swiperefresh.isRefreshing = it
        }
        viewModel.message.observe(viewLifecycleOwner){
            if (PreferenceData.getInstance().getUserItem(requireContext()) == null) {
                Navigation.findNavController(requireView()).navigate(R.id.action_global_loginFragment)
            }
        }
    }
}