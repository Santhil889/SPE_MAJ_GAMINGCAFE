package com.example.gamingcafe.repo.auth;

import com.example.gamingcafe.model.auth.Creds;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredsRepo extends JpaRepository<Creds,Integer> {

    Creds findByUsername(String username);

    Creds findByEmail(String email);

    Creds findById(int id);

}
