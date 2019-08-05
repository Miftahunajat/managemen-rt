package com.pens.managementmasyrakat.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.IuranBulanIniResponse
import kotlinx.android.synthetic.main.fragment_iuran.*
import kotlinx.android.synthetic.main.fragment_iuran.view.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class IuranFragment : Fragment() {

    val stringsTitle = arrayOf("Belum membayar Iuran", "Sudah membayar Iuran", "Belum membayar arisan")
    val drawableCircle = arrayOf(R.drawable.background_red_line_soft, R.drawable.background_green_line_soft, R.drawable.background_orange_line_soft)
    val textColor = arrayOf(R.color.red_500, R.color.green_500, R.color.orange_500)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.pens.managementmasyrakat.R.layout.fragment_iuran, container, false)
        val kkId = context!!.getUser()!!.user_kk_id

        val cal = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMMM", Locale.getDefault())
        val yearDate = SimpleDateFormat("yyyy", Locale.getDefault())
        val month_name = month_date.format(cal.getTime())
        val year_name = yearDate.format(cal.getTime())
//        Log.e("@@@", user?.toString())
        Repository.getAllIuranBulanIni(kkId!!, month_name, year_name).observe(this, androidx.lifecycle.Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.i("Loggin", it.status.toString())
                }
                Resource.SUCCESS ->{
                    setupViewWithData(view, it.data)
                }
                Resource.ERROR ->{
                    context?.showmessage("Tidak Terhubung Internet")
                    Log.i("Error", it.message!!)
                }
            }
        })
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

    private fun setupViewWithData(view: View, data: IuranBulanIniResponse?) {
        setView(view.circle_1, view.tv_1, data!!.iuran_sosial_bulan_ini.toInt())
        setView(view.circle_2, view.tv_2, data.iuran_sampah_bulan_ini.toInt())
        setView(view.circle_3, view.tv_3, data.iuran_arisan)
        if (data.iuran_arisan != 2) tv_daftar_arisan.visibility = View.GONE
        Log.d("!!!", data.toString())
    }

    private fun setView(circleView: ImageView, textView: TextView, index: Int){
        val drawableD = ContextCompat.getDrawable(context!!,drawableCircle[index])
        circleView.setImageDrawable(drawableD)
        textView.text = stringsTitle[index]
        textView.setTextColor(ContextCompat.getColor(circleView.context, textColor[index]))
//        view.circle_1.setImageDrawable(drawableCircle[data.iuranSosialBulanIni])
    }
}