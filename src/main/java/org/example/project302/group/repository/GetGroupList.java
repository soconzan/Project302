package org.example.project302.group.repository;

import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface GetGroupList {

     public Long getUniv_id();
     public Long getPd_id(); // pd_id -> pdId
     public String getPd_name(); // pd_name -> pdName
     public String getPd_content(); // pd_content -> pdContent

     public Integer getViews();

     public Integer getPd_price();
     public LocalDateTime getCreat_date(); // creat_date -> creatDate
     public LocalDateTime getPull_up_date(); // pull_up_date -> pullUpDate
     public Float getLatitude();
     public Float getLongitude();
     public String getDetail_addr(); // detail_addr -> detailAddr
     public String getCtgr_name(); // ctgr_name -> ctgrName
     public Integer getMin_people(); // min_people -> minPeople
     public Integer getMax_people(); // max_people -> maxPeople
     public Boolean getRound_yn(); // round_yn -> roundYn
     public Integer getClos_date(); // clos_date -> closDate
     public String getFile_id(); // file_name -> fileName
     public Boolean getThumbnail_yn();
     public Float getUniv_latitude();
     public Float getUniv_longitude();
     public Integer getChat_count();
     public DealStatus getDeal_status();

     public String getPrice_range();
    
}
