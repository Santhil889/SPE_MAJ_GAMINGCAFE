package com.example.gamingcafe.model.gamer;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gamerdetails")
public class GamerDetails {
    @Id
    private int gid;
    @NotNull
    private String gamertag;

    private String firstname,lastname;

    private int age;

    private String level;

    private String gender;
}
