package org.example.project302.group.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface GetGroupOttList {

    public Long getUniv_id();
    public Long getPd_id(); // pd_id -> pdId
    public String getPd_name(); // pd_name -> pdName
    public String getPd_content(); // pd_content -> pdContent
    public Integer getViews();
    public Integer getPd_price();
    public LocalDateTime getCreat_date(); // creat_date -> creatDate
    public LocalDateTime getPull_up_date(); // pull_up_date -> pullUpDate
    public String getDetail_addr(); // detail_addr -> detailAddr
    public String getCtgr_name(); // ctgr_name -> ctgrName
    public Integer getMin_people(); // min_people -> minPeople
    public Integer getMax_people(); // max_people -> maxPeople
    public Boolean getRound_yn(); // round_yn -> roundYn
    public LocalDateTime getClos_date(); // clos_date -> closDate
    public Integer getD_day();
    public Integer getChat_count();
}
