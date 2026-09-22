package org.example.project302.group.repository;

import org.example.project302.file.entity.File;

import java.time.LocalDateTime;
import java.util.List;

public interface GetOneGroup {

    public String getNickname();

    public Long getUniv_id();
    public Long getPd_id(); // pd_id -> pdId
    public String getPd_name(); // pd_name -> pdName

    public String getDeal_status();

    public String getPd_content(); // pd_content -> pdContent

    public Integer getPd_price();

    public LocalDateTime getCreat_date(); // creat_date -> creatDate
    public LocalDateTime getPull_up_date(); // pull_up_date -> pullUpDate

    public Integer getViews();
    public Float getLatitude();
    public Float getLongitude();
    public String getDetail_addr(); // detail_addr -> detailAddr
    public String getCtgr_name(); // ctgr_name -> ctgrName
    public Integer getMin_people(); // min_people -> minPeople
    public Integer getMax_people(); // max_people -> maxPeople
    public Boolean getRound_yn(); // round_yn -> roundYn
    public Integer getD_day();
    public LocalDateTime getClos_date(); // clos_date -> closDate
    public String getFile_id(); // file_name -> fileName
    public String getUniv_addr();
    public Integer getLike_count();
    public Integer getChat_count();
    public Long getUser_id();
}
