package org.example.project302.product.repository;

import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;

import java.time.LocalDateTime;

public interface GetProduct {
    public String getNickname();

    public Long getPd_id(); // pd_id -> pdId

    public String getPd_name(); // pd_name -> pdName

    public String getPd_content();

    public Integer getPd_price();

    public LocalDateTime getCreat_date();

    public LocalDateTime getPull_up_date();

    public Integer getViews();

    public Float getLatitude();

    public Float getLongitude();

    public String getDetail_addr();

    public String getCtgr_name();

    public String getFile_id();

    public ProductStatus getPd_status();

    public Integer getLike_count();

    public DealStatus getDeal_satus();

    public Long getUser_id();

}
