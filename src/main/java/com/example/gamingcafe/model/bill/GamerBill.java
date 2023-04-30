package com.example.gamingcafe.model.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gamerbill")
@IdClass(GamerBill.class)
public class GamerBill implements Serializable {
    @Id
    private int gamerid;
    @Id
    private int billid;
}


