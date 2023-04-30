package com.example.gamingcafe.repo.gamer;

import com.example.gamingcafe.model.gamer.GamerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamerDetailsRepo extends JpaRepository<GamerDetails,Integer> {
    public GamerDetails findByGid(int gid);
}
