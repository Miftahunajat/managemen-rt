package com.pens.managementmasyrakat.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pens.managementmasyrakat.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class IuranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_iuran, container, false)
//        view.rv_iuran.layoutManager = LinearLayoutManager(context)
//        val iuranAdapter = IuranAdapter()
//        var listIuran = ArrayList<Iuran>()
//        listIuran.add(Iuran("Iuran Sosial",R.drawable.ic_social, R.color.blue_500))
//        listIuran.add(Iuran("Iuran Sampah",R.drawable.ic_trash, R.color.green_500))
//        iuranAdapter.swapData(listIuran)
//        view.rv_iuran.adapter = iuranAdapter
//        view.rv_iuran.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        iuranAdapter.notifyDataSetChanged()

        return view
    }
}