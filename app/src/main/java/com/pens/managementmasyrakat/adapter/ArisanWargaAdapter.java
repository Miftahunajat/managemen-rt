package com.pens.managementmasyrakat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.pens.managementmasyrakat.R;
import com.pens.managementmasyrakat.extension.ExtensionKt;
import com.pens.managementmasyrakat.extension.ExtensionTextViewKt;
import com.pens.managementmasyrakat.network.model.AllUserArisanResponse;
import com.pens.managementmasyrakat.network.model.Arisan;
import com.pens.managementmasyrakat.network.model.Pengeluaran;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class ArisanWargaAdapter<V extends Map<? extends Arisan, ? extends List<AllUserArisanResponse>>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_LISTITEM = 1;

    protected V data;
    protected int[] sectionsStart;
    protected Object[] sections;
    protected int count;

    private OnClickListener onClickListener;

    public ArisanWargaAdapter(V data, OnClickListener listener) {
        this.data = data;
        onSetData();
        onClickListener = listener;
    }

    public void swapData(V data) {
        this.data = data;
        onSetData();
        notifyDataSetChanged();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvMinimumIuran;
        private TextView tvTanggal;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvMinimumIuran = itemView.findViewById(R.id.tv_minimum_iuran);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
        }

        public void setGroupDetails(Object item) {
            Arisan arisan = (Arisan) item;
            tvTitle.setText(arisan.getNama());
            tvMinimumIuran.setText(ExtensionKt.toRupiahs(arisan.getIuran()));
            tvTanggal.setText(ExtensionKt.formatToDate(arisan.getSelesai(), "yyyy-MM-dd","dd MMMM yyyy"));
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNama;
        private TextView tvAlamat;
        private TextView tvSudahDitarik;
        private TextView tvStatusBayar;

        ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvSudahDitarik = itemView.findViewById(R.id.tv_sudah_ditarik);
            tvStatusBayar = itemView.findViewById(R.id.tv_status_bayar);
        }

        public void setChildDetails(Object item) {
            AllUserArisanResponse userArisanResponse = (AllUserArisanResponse) item;
            tvNama.setText(userArisanResponse.getNama_peserta());
            tvAlamat.setText(userArisanResponse.getUser().getAlamat());
            int visibility = userArisanResponse.getTarik() ? View.VISIBLE : View.INVISIBLE;
            tvSudahDitarik.setVisibility(visibility);
            if (!userArisanResponse.getIkut()) ExtensionTextViewKt.toBelumVerifikasi(tvStatusBayar);
            else if (userArisanResponse.getSudah_membayar()) ExtensionTextViewKt.toSudahMembayar(tvStatusBayar);
            else ExtensionTextViewKt.toBelumMembayar(tvStatusBayar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) { // for call layout
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gelombang_arisan_warga, parent, false);
            return new GroupViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_data_warga_arisan, parent, false);
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
                if (getItemViewType(position) == VIEW_TYPE_HEADER) onClickListener.onHeaderClick(((Arisan)getItem(position)).getId());
                else if (((AllUserArisanResponse)getItem(position)).getIkut()) onClickListener.onChildClick(((AllUserArisanResponse)getItem(position)).getId());
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

    public interface OnClickListener {
        void onHeaderClick(int arisanUserId);
        void onChildClick(int position);
    }
}