package com.example.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder>{

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c, ArrayList<MyTicket> p){
        context = c;
        myTickets = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_mytiket,
                parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.xnama_wisata.setText(myTickets.get(position).getNama_wisata());
        holder.xlokasi.setText(myTickets.get(position).getLokasi());
        holder.xjumlah_tiket.setText(myTickets.get(position).getJumlah_tiket() + " Ticket");

        final String getNamaWisata = myTickets.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticketdetails = new Intent(context, MyTicketDetailAct.class);
                gotomyticketdetails.putExtra("nama_wisata", getNamaWisata);
                context.startActivity(gotomyticketdetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_wisata, xlokasi, xjumlah_tiket;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
        xlokasi = itemView.findViewById(R.id.xlokasi);
        xjumlah_tiket = itemView.findViewById(R.id.xjumlah_tiket);

    }
    }

}
