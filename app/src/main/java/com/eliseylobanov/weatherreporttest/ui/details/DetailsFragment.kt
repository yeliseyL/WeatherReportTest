package com.eliseylobanov.weatherreporttest.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eliseylobanov.weatherreporttest.R
import com.eliseylobanov.weatherreporttest.databinding.DetailsFragmentBinding
import com.eliseylobanov.weatherreporttest.model.City
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsFragment : Fragment(R.layout.details_fragment) {

    private val viewModel: DetailsViewModel by viewModel {
        parametersOf(arguments?.getParcelable<City>("selectedCity"))
    }

    lateinit var binding: DetailsFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

}