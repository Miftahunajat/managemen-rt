package com.pens.managementmasyrakat


import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.pens.managementmasyrakat.screens.AdminIuranFragmentDirections
import com.pens.managementmasyrakat.screens.AdminIuranFragmentDirections.actionAdminIuranFragmentToSearchWargaFragment2
import kotlinx.android.synthetic.main.fragment_pengurus_rt.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class PengurusRTFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengurus_rt, container, false)
        view.cv_laporan_kas_rt.setOnClickListener {
            it.findNavController().navigate(
                PengurusRTFragmentDirections.actionPengurusRTFragmentToKasRTManagementFragment(true))
        }

        view.cv_pengunguman.setOnClickListener {
            it.findNavController().navigate(PengurusRTFragmentDirections.actionPengurusRTFragmentToPengungumanBaruFragment())
        }
        return view
    }
}
