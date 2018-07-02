package cn.tickets.repository;

import cn.tickets.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    @Modifying
    @Query(value = "update MemberEntity m set m.qualification = :qualification where m.id = :id")
    void updateQualification(@Param("id") int id, @Param("qualification") int qualification);

    @Modifying
    @Query(value = "update MemberEntity m set m.level = :newLevel where m.id = :id")
    void updateLevel(@Param("id") int id, @Param("newLevel") int level);

    @Modifying
    @Query(value = "update MemberEntity m set m.email = :email, m.phone = :phone,m.name = :name where m.id = :id")
    void updateSelfInformation(@Param("id") int id, @Param("email") String email, @Param("phone") int phone, @Param("name") String name);

    MemberEntity findByEmail(String email);

    MemberEntity findByName(String name);


}
