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
import androidx.navigation.fragment.findNavController
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.extension.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UpdateIuranResponse
import kotlinx.android.synthetic.main.fragment_iuran.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class IuranFragment : Fragment() {

    val stringsTitle = arrayOf("Belum membayar Iuran", "Sudah membayar Iuran", "Belum membayar arisan")
    val drawableCircle = arrayOf(R.drawable.background_red_line_soft, R.drawable.background_green_line_soft, R.drawable.background_orange_line_soft)
    val textColor = arrayOf(R.color.red_500, R.color.green_500, R.color.orange_500)

    companion object {
        const val TYPE_SOSIAL = 1
        const val TYPE_SAMPAH = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_iuran, container, false)
        view.tv_bulan.text = Calendar.getInstance().getNamaBulan()
        val kkId = context!!.getUser()!!.user_kk_id
        view.tv_tanggal.text = Calendar.getInstance().getFormattedTanggal()
        val cal = Calendar.getInstance()
        val monthDate = SimpleDateFormat("MMMM", Locale.getDefault())
        val yearDate = SimpleDateFormat("yyyy", Locale.getDefault())
        val monthName = monthDate.format(cal.getTime())
        Log.d(IuranFragment::class.java.simpleName, "onCreateView: $");
        val yearName = yearDate.format(cal.time)
        Repository.getAllIuranBulanIni(kkId, monthName, yearName).observe(this, androidx.lifecycle.Observer {
            when(it?.status){
                Resource.LOADING ->{
                    view.cl_1.toLoading()
                    view.cl_2.toLoading()
                    Log.i("Loggin", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.cl_1.finishLoading()
                    view.cl_2.finishLoading()
                    setupViewWithData(view, it.data)
                }
                Resource.ERROR ->{
                    view.cl_1.finishLoading()
                    view.cl_2.finishLoading()
                    context?.showmessage("Tidak Terhubung Internet")
                    Log.i("Error", it.message!!)
                }
            }
        })
        view.cl_1.setOnClickListener {
            findNavController().navigate(IuranFragmentDirections.actionIuranFragmentToDataIuranDetailFragment(
                TYPE_SOSIAL, context?.getUser()!!.user_kk_id, TYPE_SOSIAL))
        }
        view.cl_2.setOnClickListener {
            findNavController().navigate(IuranFragmentDirections.actionIuranFragmentToDataIuranDetailFragment(
                TYPE_SAMPAH, context?.getUser()!!.user_kk_id, TYPE_SAMPAH))
        }

        return view
    }

    private fun setupViewWithData(view: View, data: UpdateIuranResponse?) {
        setView(view.circle_1, view.tv_1, data!!.iuran_sosial_bulan_ini.toInt())
        setView(view.circle_2, view.tv_2, data.iuran_sampah_bulan_ini.toInt())
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