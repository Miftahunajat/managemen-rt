package com.pens.managementmasyrakat.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.pens.managementmasyrakat.R
import kotlinx.android.synthetic.main.fragment_admin_arisan.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AdminArisanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_arisan, container, false)
        view.tv_tambah_gelombang.setOnClickListener {
            it.findNavController().navigate(AdminArisanFragmentDirections.actionAdminArisanFragmentToTambahArisanFragment())
        }
        // Inflate the layout for this fragment
        return view
    }


}
