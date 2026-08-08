package dev.pranavsharma.librarymanagementsystem.service.impl;

import dev.pranavsharma.librarymanagementsystem.mapper.SubscriptionPlanMapper;
import dev.pranavsharma.librarymanagementsystem.model.SubscriptionPlan;
import dev.pranavsharma.librarymanagementsystem.model.User;
import dev.pranavsharma.librarymanagementsystem.payload.dto.SubscriptionPlanDTO;
import dev.pranavsharma.librarymanagementsystem.repository.SubscriptionPlanRepository;
import dev.pranavsharma.librarymanagementsystem.repository.SubscriptionRepository;
import dev.pranavsharma.librarymanagementsystem.service.SubscriptionPlanService;
import dev.pranavsharma.librarymanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanMapper planMapper;
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) throws Exception {

        if (planRepository.existsByPlanCode(planDTO.getPlanCode())) {
            throw new Exception("Plan code already exists!");
        }

        SubscriptionPlan plan = planMapper.toEntity(planDTO);

        User currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getEmail());
        plan.setUpdatedBy(currentUser.getEmail());
        SubscriptionPlan savedPlan = planRepository.save(plan);
        return planMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long planId, SubscriptionPlanDTO planDTO) throws Exception {

        SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(
                () -> new Exception("Plan not found!")
        );
        planMapper.updateEntity(existingPlan, planDTO);
        User currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getEmail());
        SubscriptionPlan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toDTO(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {


        SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(
                () -> new Exception("Plan not found!")
        );
        planRepository.delete(existingPlan);

    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlan() {
        List<SubscriptionPlan> planList = planRepository.findAll();
        return planList.stream().map(
                planMapper::toDTO
        ).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception {
        SubscriptionPlan plan = planRepository.findByPlanCode(subscriptionPlanCode);

        if (plan == null) {
            throw new Exception("Plan not found!");
        }
        return plan;
    }
}
