package cn.tickets.repository;

import cn.tickets.entity.AccountEntity;
import cn.tickets.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface AccountRepository extends CrudRepository<AccountEntity, Integer> {
    AccountEntity findByEmail(String email);

    AccountEntity findByEmailAndPassword(String email, String password);

    boolean existsByIdAndPassword(int id, String password);

    AccountEntity findByIdAndPassword(int id,String password);
}
