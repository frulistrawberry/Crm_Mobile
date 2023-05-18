package com.baihe.lihepro.entity;

import java.io.Serializable;
import java.util.List;

public class TeamUser implements Serializable {
    private String category_id;
    private String category_name;
    private List<String> team_ids;
    private String team_user;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<String> getTeam_ids() {
        return team_ids;
    }

    public void setTeam_ids(List<String> team_ids) {
        this.team_ids = team_ids;
    }

    public String getTeam_user() {
        return team_user;
    }

    public void setTeam_user(String team_user) {
        this.team_user = team_user;
    }
}
