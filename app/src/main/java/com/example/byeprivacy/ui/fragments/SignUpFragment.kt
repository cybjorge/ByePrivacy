package com.example.byeprivacy.ui.fragments

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentSignUpBinding
import com.example.byeprivacy.utils.EventHandler
import com.example.byeprivacy.utils.Injection
import com.example.byeprivacy.ui.viewmodels.LoginViewModel
import com.example.byeprivacy.ui.viewmodels.SignUpViewModel

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,Injection.provideViewModelFactory(requireContext())).get(
            SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
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
        binding.signupbutton.setOnClickListener {
            if(
                binding.newUsername.text.toString().isNotBlank() &&
                binding.newPassword.text.toString().isNotBlank() &&
                binding.newPasswordAgain.text.toString().isNotBlank() &&
                binding.newPassword.text.toString().compareTo(binding.newPasswordAgain.text.toString())==0
            ){
                viewModel.sign_up(
                    binding.newUsername.text.toString(),
                    binding.newPassword.text.toString()
                )
            }else if (binding.newUsername.text.toString().isBlank() || binding.newPassword.text.toString().isBlank()){
                viewModel.show("Fill in name and password")
            } else {
                viewModel.show("Passwords must be same")
            }
        }
        viewModel.user.observe(viewLifecycleOwner){
            it?.let {
                PreferenceData.getInstance().putUserItem(requireContext(),it)
                Navigation.findNavController(requireView()).navigate(R.id.action_global_barsFragment)
            }
        }
    }


}