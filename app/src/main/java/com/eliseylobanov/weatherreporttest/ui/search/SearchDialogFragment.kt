package com.eliseylobanov.weatherreporttest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.eliseylobanov.weatherreporttest.R
import com.eliseylobanov.weatherreporttest.databinding.SearchDialogFragmentBinding
import com.eliseylobanov.weatherreporttest.ui.main.MainViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchDialogFragment : DialogFragment(R.layout.search_dialog_fragment) {

    private val viewModel: MainViewModel by sharedViewModel()
    lateinit var binding: SearchDialogFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = SearchDialogFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonOk.setOnClickListener {
            viewModel.searchCity(binding.inputEditText.text.toString())
            dismiss()
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}