package com.example.gamingcafe.model.games;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gid;
    @Column(unique = true)
    @NotNull
    private String gamename;
    @NotNull
    private int cid;
    @NotNull
    private int cost;
}
