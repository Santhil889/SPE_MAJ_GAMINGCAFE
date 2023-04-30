package com.example.gamingcafe.repo.bill;

import com.example.gamingcafe.model.bill.GamerBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GamerBillRepo extends JpaRepository<GamerBill,GamerBill> {
    @Query("select g from GamerBill g where g.gamerid = ?1")
    public List<GamerBill> xyzfalana(@Param("id") int id);

    public List<GamerBill> findAllByGamerid(int gid);
}
