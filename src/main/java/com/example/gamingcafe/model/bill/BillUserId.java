package com.example.gamingcafe.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillUserId implements Serializable {
    private Integer gid;
    private Integer billid;
    private Integer gameid;
}