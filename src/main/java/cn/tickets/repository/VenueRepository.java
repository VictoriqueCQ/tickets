package cn.tickets.repository;

import cn.tickets.entity.VenueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;


public interface VenueRepository extends JpaRepository<VenueEntity, Integer> {

    @Modifying
    @Query(value = "update VenueEntity v set v.approve = :approve where v.id = :id")
    void approve(@Param("id") int id, @Param("approve") int approve);

    Page<VenueEntity> findByApprove(int approve, Pageable page);

    List<VenueEntity> findByApprove(int approve);

    VenueEntity findBySystemid(String systemid);

    VenueEntity findById(int id);

}
