package org.example.inplrz.controller;

import org.example.inplrz.dto.CreatePlanRequest;
import org.example.inplrz.dto.UpdatePlanStatusRequest;
import org.example.inplrz.entity.*;
import org.example.inplrz.repository.ActivityRepository;
import org.example.inplrz.repository.AuditLogRepository;
import org.example.inplrz.repository.PlanRepository;
import org.example.inplrz.repository.UserRepository;
import org.example.inplrz.specification.PlanSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final AuditLogRepository auditLogRepository;

    public PlanController(PlanRepository planRepository,
                          UserRepository userRepository,
                          ActivityRepository activityRepository,
                          AuditLogRepository auditLogRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody CreatePlanRequest request) {
        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден!"));

        Plan newPlan = new Plan();
        newPlan.setEmployee(employee);
        newPlan.setPeriodStart(request.getPeriodStart());
        newPlan.setPeriodEnd(request.getPeriodEnd());

        Plan savedPlan = planRepository.save(newPlan);
        return ResponseEntity.ok(savedPlan);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Plan>> getEmployeePlans(@PathVariable UUID employeeId) {
        List<Plan> plans = planRepository.findByEmployeeId(employeeId);
        return ResponseEntity.ok(plans);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Plan> updatePlanStatus(
            @PathVariable UUID id,
            @RequestBody UpdatePlanStatusRequest request){
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("План не найден!"));
        plan.setStatus(request.getStatus());
        Plan updatedPlan = planRepository.save(plan);
        return ResponseEntity.ok(updatedPlan);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Plan> closePlan(@PathVariable UUID id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("План не найден!"));

        List<Activity> activities = activityRepository.findByPlanId(id);

        long total = activities.stream()
                .filter(a -> a.getStatus() != ActivityStatus.CANCELED)
                .count();

        long completed = activities.stream()
                .filter(a -> a.getStatus() == ActivityStatus.DONE_AND_CONFIRMED)
                .count();

        int percentage = total > 0 ? (int) Math.round((double) completed / total * 100) : 0;

        PlanStatus oldStatus = plan.getStatus();

        if (percentage == 100) {
            plan.setStatus(PlanStatus.COMPLETED);
        } else if (percentage > 0) {
            plan.setStatus(PlanStatus.PARTIALLY_COMPLETED);
        } else {
            plan.setStatus(PlanStatus.CLOSED);
        }

        Plan savedPlan = planRepository.save(plan);

        // Запись в аудит истории изменений
        AuditLog auditLog = new AuditLog();
        auditLog.setPlan(savedPlan);
        auditLog.setUser(savedPlan.getEmployee());
        auditLog.setActionType("CLOSE_PLAN");
        auditLog.setComment("Автоматическое закрытие периода. Процент выполнения: " + percentage + "%");
        auditLog.setChangedFields("{\"oldStatus\": \"" + oldStatus + "\", \"newStatus\": \"" + savedPlan.getStatus() + "\"}");
        auditLog.setPlanVersion(savedPlan.getVersion());

        auditLogRepository.save(auditLog);

        return ResponseEntity.ok(savedPlan);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Plan>> searchPlans(
            @RequestParam(required = false) PlanStatus status,
            @RequestParam(required = false) String employeeName) {

        Specification<Plan> spec = PlanSpecification.filterPlans(status, employeeName);
        List<Plan> plans = planRepository.findAll(spec);

        return ResponseEntity.ok(plans);
    }
}