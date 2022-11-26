package com.example.byeprivacy.ui.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentLoginBinding
import com.example.byeprivacy.utils.Injection
import com.example.byeprivacy.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,Injection.provideViewModelFactory(requireContext())).get(
            LoginViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isNotBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_global_barsFragment)
            return
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
        }
        binding.login.setOnClickListener {
            if (binding.username.text.toString().isNotBlank() && binding.password.text.toString().isNotBlank()){
                viewModel.login(
                    binding.username.text.toString(),
                    binding.password.text.toString()
                )
            }
            else{
                viewModel.show("Fill in name and password")
            }
        }
        binding.signupreference.setOnClickListener{
            it.findNavController().navigate(R.id.action_global_signUpFragment)
        }

        viewModel.user.observe(viewLifecycleOwner){
            it?.let {
                PreferenceData.getInstance().putUserItem(requireContext(),it)
                Navigation.findNavController(requireView()).navigate(R.id.action_global_barsFragment)
            }
        }
    }

}