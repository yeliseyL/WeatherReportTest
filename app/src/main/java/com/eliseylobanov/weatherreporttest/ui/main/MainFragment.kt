package com.eliseylobanov.weatherreporttest.ui.main

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eliseylobanov.weatherreporttest.R
import com.eliseylobanov.weatherreporttest.WeatherApiStatus
import com.eliseylobanov.weatherreporttest.databinding.MainFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private val viewModel: MainViewModel by viewModel()

    lateinit var binding: MainFragmentBinding
    lateinit var adapter: CitiesAdapter
    lateinit var locationManager: LocationManager

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            viewModel.searchCurrentCity(location.latitude.toString(), location.longitude.toString())
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        getLocation()

        binding = MainFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = CitiesAdapter(CitiesAdapter.OnClickListener {
            viewModel.displayCityDetails(it)
        })

        binding.recyclerCities.adapter = adapter

        viewModel.navigateToSelectedCity.observe(viewLifecycleOwner, {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetails(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        viewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                WeatherApiStatus.LOADING -> binding.progress.visibility = View.VISIBLE
                WeatherApiStatus.DONE -> binding.progress.visibility = View.GONE
                WeatherApiStatus.ERROR ->
                    Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.searchDialogFragment)
        }

        return binding.root
    }

    private fun getLocation() {
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener)
        } catch (ex: SecurityException) {
            val permission = ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private val permissionsResultCallback = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
        when (it) {
            true -> { getLocation() }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}