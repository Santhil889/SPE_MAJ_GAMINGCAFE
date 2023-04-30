package com.example.gamingcafe.model.bill;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(BillUserId.class)
@Table(name = "billuser")
public class BillUser {
    @Id
    private Integer gid;
    @Id
    private Integer billid;
    @Id
    private Integer gameid;
    @NotNull
    private int hours;
    @NotNull
    private int total;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date filledtime;

}