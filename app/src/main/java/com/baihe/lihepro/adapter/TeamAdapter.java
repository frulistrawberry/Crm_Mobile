package com.baihe.lihepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baihe.lihepro.R;
import com.baihe.lihepro.entity.TeamUser;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.Holder>{

    private LayoutInflater inflater;
    private Context context;

    private List<TeamUser> rows;
    private OnTeamOption listener;
    private int type;

    private boolean showAdd;

    public interface OnTeamOption{
        void add();
        void edit(String teamUsers,String teamUserText,String category,String categoryText);
        void delete(String teamUsers,String teamUserText,String category,String categoryText);
    }

    public TeamAdapter(Context context,int type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.rows = new ArrayList<>();
        this.type = type;
    }

    public void setData(List<TeamUser> data){
        this.rows = data;
        notifyDataSetChanged();
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
    }

    @NonNull
    @Override
    public TeamAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeamAdapter.Holder(inflater.inflate(R.layout.item_team, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamAdapter.Holder holder, int position) {
        final TeamUser teamUser = rows.get(position);
        holder.categoryTv.setText(teamUser.getCategory_name());
        holder.userNameIv.setText(teamUser.getTeam_user().replace(","," "));
        if (position == rows.size()-1  && showAdd){
            holder.addBtn.setVisibility(View.VISIBLE);
        }else {
            holder.addBtn.setVisibility(View.GONE);
        }
        if (position == 0){
            holder.v_divider.setVisibility(View.VISIBLE);
        }else {
            holder.v_divider.setVisibility(View.GONE);
        }
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.add();
                }
            }
        });
        holder.updateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    StringBuilder teamUsers = new StringBuilder();
                    for (String team_id : teamUser.getTeam_ids()) {
                        teamUsers.append(team_id);
                        teamUsers.append(",");
                    }
                    teamUsers.deleteCharAt(teamUsers.lastIndexOf(","));
                    listener.edit(teamUsers.toString(),teamUser.getTeam_user(),teamUser.getCategory_id(),teamUser.getCategory_name());
                }
            }
        });

        holder.delIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    StringBuilder teamUsers = new StringBuilder();
                    for (String team_id : teamUser.getTeam_ids()) {
                        teamUsers.append(team_id);
                        teamUsers.append(",");
                    }
                    teamUsers.deleteCharAt(teamUsers.lastIndexOf(","));
                    listener.delete(teamUsers.toString(),teamUser.getTeam_user(),teamUser.getCategory_id(),teamUser.getCategory_name());
                }
            }
        });
    }

    public void setListener(OnTeamOption listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView delIv;
        private ImageView updateIv;
        private TextView categoryTv;
        private TextView userNameIv;
        private LinearLayout addBtn;
        private View v_divider;

        public Holder(@NonNull View itemView) {
            super(itemView);
            delIv = itemView.findViewById(R.id.iv_del);
            updateIv = itemView.findViewById(R.id.iv_update);
            categoryTv = itemView.findViewById(R.id.tv_category);
            userNameIv = itemView.findViewById(R.id.tv_user);
            addBtn = itemView.findViewById(R.id.ll_add);
            v_divider = itemView.findViewById(R.id.v_divider);
        }
    }




}
