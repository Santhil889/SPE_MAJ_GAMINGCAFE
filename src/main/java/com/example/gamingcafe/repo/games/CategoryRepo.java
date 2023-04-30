package com.example.gamingcafe.repo.games;

import com.example.gamingcafe.model.games.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
    public Category findByCid(int cid);
}
