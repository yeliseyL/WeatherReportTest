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
import androidx.core.app.ActivityCompat
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

    companion object {
        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
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
                WeatherApiStatus.ERROR -> {
                    View.GONE;
                    Toast.makeText(context, getString(R.string.no_connection), Toast.LENGTH_LONG).show()
                }
            }
        })

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.searchDialogFragment)
            getLocation()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (
                grantResults.isEmpty() ||
                grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
                (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                        grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                        PackageManager.PERMISSION_DENIED)) {
            Toast.makeText(context, getString(R.string.security_exception), Toast.LENGTH_LONG).show()
        } else {
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    locationListener)
        } catch (ex: SecurityException) {
            if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) !==
                    PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                }
            }
        }
    }
}