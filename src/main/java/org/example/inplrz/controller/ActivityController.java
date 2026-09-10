package org.example.inplrz.controller;

import org.example.inplrz.dto.*;
import org.example.inplrz.entity.*;
import org.example.inplrz.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final PlanRepository planRepository;
    private final CompetenceRepository competenceRepository;
    private final ActivityTypeRepository activityTypeRepository;

    public ActivityController(ActivityRepository activityRepository,
                              PlanRepository planRepository,
                              CompetenceRepository competenceRepository,
                              ActivityTypeRepository activityTypeRepository) {
        this.activityRepository = activityRepository;
        this.planRepository = planRepository;
        this.competenceRepository = competenceRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody CreateActivityRequest request) {
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("План не найден!"));

        Competence competence = competenceRepository.findById(request.getCompetenceId())
                .orElseThrow(() -> new RuntimeException("Компетенция не найдена!"));

        ActivityType type = activityTypeRepository.findById(request.getTypeId())
                .orElseThrow(() -> new RuntimeException("Тип мероприятия не найден!"));


        Activity newActivity = new Activity();
        newActivity.setPlan(plan);
        newActivity.setCompetence(competence);
        newActivity.setType(type);
        newActivity.setName(request.getName());
        newActivity.setStartDate(request.getStartDate());
        newActivity.setDeadline(request.getDeadline());



        Activity savedActivity = activityRepository.save(newActivity);
        return ResponseEntity.ok(savedActivity);
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<Activity>> getActivitiesByPlanId(@PathVariable UUID planId) {
        List<Activity> activities = activityRepository.findByPlanId(planId);
        return ResponseEntity.ok(activities);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Activity> updateActivityStatus(@PathVariable UUID id,@RequestBody UpdateActivityStatusRequest request){
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено!"));
        activity.setStatus(request.getStatus());
        Activity updatedActivity = activityRepository.save(activity);
        return ResponseEntity.ok(updatedActivity);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Activity> completeActivity(@PathVariable UUID id, @RequestBody CompleteActivityRequest request) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено!"));

        activity.setFactEndDate(request.getFactEndDate());
        activity.setActualResult(request.getActualResult());
        activity.setStatus(ActivityStatus.ON_CONFIRMATION);

        Activity savedActivity = activityRepository.save(activity);
        return ResponseEntity.ok(savedActivity);
    }
    // Подтверждение или возврат на доработку руководителем
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Activity> confirmActivity(
            @PathVariable UUID id,
            @RequestBody ConfirmActivityRequest request) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено!"));

        activity.setStatus(request.getStatus());

        // Сохраняем комментарий, если руководитель его написал
        if (request.getManagerComment() != null) {
            activity.setManagerComment(request.getManagerComment());
        }

        Activity savedActivity = activityRepository.save(activity);
        return ResponseEntity.ok(savedActivity);
    }

    @GetMapping("/plan/{planId}/progress")
    public ResponseEntity<PlanProgressResponse> getPlanProgress(@PathVariable UUID planId) {
        List<Activity> activities = activityRepository.findByPlanId(planId);

        long total = activities.stream()
                .filter(a -> a.getStatus() != ActivityStatus.CANCELED)
                .count();

        long completed = activities.stream()
                .filter(a -> a.getStatus() == ActivityStatus.DONE_AND_CONFIRMED)
                .count();

        int percentage = 0;
        if (total > 0) {
            percentage = (int) Math.round((double) completed / total * 100);
        }

        return ResponseEntity.ok(new PlanProgressResponse(total, completed, percentage));
    }
}