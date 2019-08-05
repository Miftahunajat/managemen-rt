package com.pens.managementmasyrakat.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.pens.managementmasyrakat.R
import kotlinx.android.synthetic.main.fragment_admin_iuran.view.*
import kotlinx.android.synthetic.main.fragment_data_iuran_detail.view.group_choices


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AdminIuranFragment : Fragment() {

    companion object  {
        const val TYPE_SOSIAL = 1
        const val TYPE_SAMPAH= 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_iuran, container, false)
        view.cv_sosial.setOnClickListener {
            it.findNavController().navigate(AdminIuranFragmentDirections.actionAdminIuranFragmentToSearchWargaFragment2(
                TYPE_SOSIAL))
        }

        view.cv_sampah.setOnClickListener {
            it.findNavController().navigate(AdminIuranFragmentDirections.actionAdminIuranFragmentToSearchWargaFragment2(
                TYPE_SAMPAH))
        }
        return view
    }


}
