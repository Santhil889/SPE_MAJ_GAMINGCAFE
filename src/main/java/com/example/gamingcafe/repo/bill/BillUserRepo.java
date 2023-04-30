package com.example.gamingcafe.repo.bill;

import com.example.gamingcafe.model.bill.BillUser;
import com.example.gamingcafe.model.bill.BillUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillUserRepo extends JpaRepository<BillUser, BillUserId> {

    public List<BillUser> findAllByGidAndBillid(int gid,int billid);
}
