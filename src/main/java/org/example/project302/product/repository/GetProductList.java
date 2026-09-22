package org.example.project302.product.repository;

import org.example.project302.product.entity.DealStatus;
import org.example.project302.product.entity.ProductStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface GetProductList {
    public Long getUser_id();
    public Long getUniv_id();

    public Long getPd_id(); // pd_id -> pdId

    public String getPd_name(); // pd_name -> pdName

    public String getPd_price();

    public LocalDateTime getCreat_date();

    public LocalDateTime getPull_up_date();

    public Integer getPull_up_cnt();

    public Integer getPd_views();

    public String getCtgr_name();

    public String getFile_id();

    public Integer getLike_count();

    public DealStatus getDeal_status();

    public ProductStatus getPd_status();


}
