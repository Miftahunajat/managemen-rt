package com.pens.managementmasyrakat.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.isAdminArisan
import com.pens.managementmasyrakat.isAdminIuran
import com.pens.managementmasyrakat.isPengurusRT
import com.pens.managementmasyrakat.network.Repository
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.inc_menu.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.circle_daftar_iuran.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToIuranFragment())
        }
        view.circle_data_diri.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDataDiriFragment())
        }
        view.circle_admin_arisan.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAdminArisanFragment())
        }
        view.circle_admin_iuran.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAdminIuranFragment())
        }
        setupUser(view)
        view.circle_pengurus_rt
        return view
    }

    private fun setupUser(view: View) {
        val user = Repository.getUser(context!!)
        view.tv_nama.text = user!!.nama
        if (user.isAdminArisan() == false) {
            view.menu_4.visibility = View.GONE
        }

        if (user.isAdminIuran() == false) {
            view.menu_5.visibility = View.GONE
        }

        if (user.isPengurusRT() == false) {
            view.menu_6.visibility = View.GONE
        }
    }


}
