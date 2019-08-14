package com.pens.managementmasyrakat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.pens.managementmasyrakat.ExtensionKt;
import com.pens.managementmasyrakat.R;
import com.pens.managementmasyrakat.network.model.DataKasRTResponse;
import com.pens.managementmasyrakat.network.model.Pengeluaran;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class KasRTRAdapter<V extends Map<? extends DataKasRTResponse, ? extends List<Pengeluaran>>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_LISTITEM = 1;

    protected V data;
    protected int[] sectionsStart;
    protected Object[] sections;
    protected int count;

    private OnBulanClickListener onBulanClickListener;

    public KasRTRAdapter(V data, OnBulanClickListener listener) {
        this.data = data;
        onSetData();
        onBulanClickListener = listener;
    }

    public void swapData(V data) {
        this.data = data;
        onSetData();
        notifyDataSetChanged();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView tvBulan;
        private TextView tvTotalSaldo;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBulan = itemView.findViewById(R.id.tv_bulan);
            tvTotalSaldo = itemView.findViewById(R.id.tv_total);
        }

        public void setGroupDetails(Object item) {
            DataKasRTResponse dataKasRTResponse = (DataKasRTResponse) item;
            tvBulan.setText(dataKasRTResponse.getBulan().getNama_bulan());
            tvTotalSaldo.setText("Total : " + ExtensionKt.toRupiahs(dataKasRTResponse.getTotal()));
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView tvKeterangan;
        private TextView tvHargaPengeluaran;

        ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_bulan);
            tvHargaPengeluaran = itemView.findViewById(R.id.tv_harga);
        }

        public void setChildDetails(Object item) {
            Pengeluaran pengeluaran = (Pengeluaran) item;
            tvKeterangan.setText(pengeluaran.getKeterangan());
            int color = Integer.valueOf(((Pengeluaran) item).getJumlah()) < 0 ? R.color.red_900 : R.color.green_900;
            tvHargaPengeluaran.setText(ExtensionKt.toRupiahs(((Pengeluaran) item).getJumlah()));
            tvHargaPengeluaran.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) { // for call layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kas_group_rt, parent, false);
            return new GroupViewHolder(view);

        } else { // for email layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kas_child_rt, parent, false);
            return new ChildViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return Arrays.binarySearch(sectionsStart, position) < 0 ? VIEW_TYPE_LISTITEM : VIEW_TYPE_HEADER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            ((GroupViewHolder) viewHolder).setGroupDetails(getItem(position));
        } else {
            ((ChildViewHolder) viewHolder).setChildDetails(getItem(position));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemViewType(position) == VIEW_TYPE_HEADER)
                    onBulanClickListener.onClick(position, ((DataKasRTResponse)getItem(position)).getBulan().getNama_bulan());
            }
        });
    }


    public Object getItem(int position) {
        int sectionIndex = getSectionForPosition(position);
        int innerIndex = position - sectionsStart[sectionIndex];
        if(innerIndex == 0) { //head
            return sections[sectionIndex];
        }
        else { //values
            return data.get(sections[sectionIndex]).get(innerIndex - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public int getPositionForSection(int section) {
        return sectionsStart[section];
    }

    public int getSectionForPosition(int position) {
        int section = Arrays.binarySearch(sectionsStart, position);
        return section < 0 ? -section - 2 : section;
    }

    protected void onSetData() {
        if(data == null) {
            sectionsStart = null;
            sections = null;
            count = 0;
        }
        else {
            sectionsStart = new int[data.size()];
            sections = data.keySet().toArray(new Object[data.size()]);
            count = 0;
            int i = 0;
            for(List<?> v : data.values()) {
                sectionsStart[i] = count;
                i++;
                count += 1 + v.size();
            }
        }
    }

    public interface OnBulanClickListener{
        void onClick(int position, String namaBulan);
    }
}