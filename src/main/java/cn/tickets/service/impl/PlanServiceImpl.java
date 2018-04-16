package cn.tickets.service.impl;

import cn.tickets.entity.PlanEntity;
import cn.tickets.entity.SeatEntity;
import cn.tickets.entity.VenueEntity;
import cn.tickets.repository.PlanRepository;
import cn.tickets.repository.SeatRepository;
import cn.tickets.repository.VenueRepository;
import cn.tickets.service.PlanService;
import cn.tickets.util.Default;
import cn.tickets.vo.PlanVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.util.*;

@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private PlanRepository planRepository;
    private VenueRepository venueRepository;
    private SeatRepository seatRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository, VenueRepository venueRepository,SeatRepository seatRepository) {
        this.planRepository = planRepository;
        this.venueRepository = venueRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public String indexplan(Model model, int id) {
        System.err.println("场馆id：" + id);
        List<PlanEntity> planEntities = planRepository.findByVid(id);
        List<PlanVO> planVOS = new ArrayList<>(planEntities.size());
        planEntities.forEach(planEntity -> {
            PlanVO planVO = new PlanVO();
            BeanUtils.copyProperties(planEntity, planVO);
            planVO.setVname(venueRepository.findOne(planEntity.getVid()).getName());
            planVOS.add(planVO);
        });

        Collections.sort(planVOS);
        model.addAttribute("indexplan", planVOS);
        return "venue/index";
    }

    @Override
    public String scenePlan(Model model, int id) {
        List<PlanEntity> planEntities = planRepository.findByVid(id);
        List<PlanVO> planVOS = new ArrayList<>(planEntities.size());
        planEntities.forEach(planEntity -> {
            PlanVO planVO = new PlanVO();
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            if (timestamp.getTime() - planEntity.getDate().getTime() < 0) {
                BeanUtils.copyProperties(planEntity, planVO);
                planVO.setVname(venueRepository.findOne(planEntity.getVid()).getName());
                planVOS.add(planVO);
            }

        });
//        System.err.println(planVOS.toString());
        model.addAttribute("sceneplan", planVOS);
        return "venue/scene";
    }

    @Override
    public Map<String, Object> savePlan(int id, Timestamp time, String type, String description, int fprice, int bprice) {
        Map<String, Object> result = new TreeMap<>();

        PlanEntity planEntity = new PlanEntity();
        planEntity.setVid(id);
        planEntity.setType(type);
        planEntity.setDate(time);
        planEntity.setDescription(description);
        planEntity.setFprice(fprice);
        planEntity.setBprice(bprice);
        planRepository.save(planEntity);
        int pid = planEntity.getPid();

        VenueEntity venueEntity = venueRepository.findOne(id);
        int fnumber = venueEntity.getFsnumber();
        int bnumber = venueEntity.getBsnumber();
        List<SeatEntity> frontSeatEntities = new ArrayList<>(fnumber);
        List<SeatEntity> backSeatEntities = new ArrayList<>(bnumber);
        String tsStr = "1980-01-01 00:00:00";
        Timestamp ts = Timestamp.valueOf(tsStr);
        for (int i = 1; i <= fnumber; i++) {
            SeatEntity seatEntity = new SeatEntity(0, id, 1, i, ts, 0, pid);
            frontSeatEntities.add(seatEntity);
        }
        for (int i = 1; i <= bnumber; i++) {
            SeatEntity seatEntity = new SeatEntity(0, id, 0, i, ts, 0, pid);
            backSeatEntities.add(seatEntity);
        }
        seatRepository.save(frontSeatEntities);
        seatRepository.save(backSeatEntities);

        result.put(Default.HTTP_RESULT, true);
        System.err.println("save plan:" + result);
        return result;
    }

    @Override
    public List<PlanVO> allMemberIndexPlan() {
        List<PlanEntity> planEntities = planRepository.findAll();
        List<PlanVO> planVOS = new ArrayList<>(planEntities.size());
        planEntities.forEach(planEntity -> {

            Timestamp timestamp = planEntity.getDate();
            Date date = new Date();
            Timestamp timestampNow = new Timestamp(date.getTime());
            long deltaTime = (timestamp.getTime() - timestampNow.getTime());
            if (deltaTime > 0) {
                PlanVO planVO = new PlanVO();
                BeanUtils.copyProperties(planEntity, planVO);
                planVO.setVname(venueRepository.findOne(planEntity.getVid()).getName());
                planVOS.add(planVO);
            }
        });
        return planVOS;
    }
}
