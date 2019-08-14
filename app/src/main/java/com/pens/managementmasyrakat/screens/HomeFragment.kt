package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
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

    var idPengunguman = 0

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
        view.ic_keluar.setOnClickListener {
            Repository.clearUser(context!!)
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
        view.tv_to_pengunguman.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPengungumanFragment(idPengunguman))
        }
        view.circle_pengurus_rt.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPengurusRTFragment())
        }
        view.circle_kas_rt.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToKasRTManagementFragment(false))
        }
        view.daftar_pengunguman.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPengungumanListFragment())
        }
        FirebaseMessaging.getInstance().subscribeToTopic("pengunguman")
        setupUser(view)
        setupPengunguman(view)
        return view
    }

    private fun setupPengunguman(view: View?) {
        Repository.getAllPengunguman().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view!!.tv_title.text = it.data!!.last().title
                    view.tv_body.text = it.data!!.last().body
                    Log.d("Success", it.data.toString())
                    idPengunguman = it.data!!.last().id
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun setupUser(view: View) {
        val user = Repository.getUser(context!!)
        val nama = user!!.nama
        view.textView7.text = "Selamat datang\n $nama"
        if (user.isAdminArisan() == false) {
            view.menu_4.visibility = View.INVISIBLE
            view.menu_4.setOnClickListener(null)
        }

        if (user.isAdminIuran() == false) {
            view.menu_5.visibility = View.INVISIBLE
            view.menu_5.setOnClickListener(null)
        }

        if (user.isPengurusRT() == false) {
            view.menu_6.visibility = View.INVISIBLE
            view.menu_6.setOnClickListener(null)
        }
    }


}
