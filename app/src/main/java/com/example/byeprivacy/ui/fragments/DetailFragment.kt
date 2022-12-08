package com.example.byeprivacy.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.byeprivacy.R
import com.example.byeprivacy.data.api.helpers.PreferenceData
import com.example.byeprivacy.databinding.FragmentDetailBinding
import com.example.byeprivacy.ui.viewmodels.DetailViewModel
import com.example.byeprivacy.ui.widgets.barDetail.AdapterDetail
import com.example.byeprivacy.ui.widgets.barDetail.BarItemDetail
import com.example.byeprivacy.utils.Injection
//TODO ui
class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBinding
    private val navigationArgs: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, Injection.provideViewModelFactory(requireContext())).get(
                DetailViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        if ((x?.uid ?: "").isBlank()) {
            Navigation.findNavController(view).navigate(R.id.action_global_loginFragment)
            return
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = viewModel

        }?.also { bind ->
            bind.toMap.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://maps.google.com/?q=".plus(
                            bind.model?.bar?.value?.lat
                        ).plus(",").plus(bind.model?.bar?.value?.lon)
                    )
                )
                requireContext().startActivity(intent)
            }
            bind.typeDetail.setOnClickListener {
                findNavController().previousBackStackEntry?.destination?.id?.let { it1 ->
                    findNavController().navigate(
                        it1
                    )
                }

            }

            Log.d("type", viewModel.type.value.toString())
        }


        Log.d("detailfragment", navigationArgs.id)
        viewModel.loadBarDetail(navigationArgs.id)
    }

}