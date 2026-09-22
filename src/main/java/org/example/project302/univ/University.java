package org.example.project302.univ;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "universities")
@AllArgsConstructor
@NoArgsConstructor
public class University {

    @Id
    private Long univId;

    private String gubun;
    private String univName;
    private String univAddr;
    private Integer univPost;
    private Float univLatitude;
    private Float univLongitude;
}
