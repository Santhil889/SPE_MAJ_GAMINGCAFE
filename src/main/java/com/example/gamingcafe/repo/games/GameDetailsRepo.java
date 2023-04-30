package com.example.gamingcafe.repo.games;

import com.example.gamingcafe.model.games.GameDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDetailsRepo extends JpaRepository<GameDetails,Integer> {
    public GameDetails findByGid(int gid);
}
