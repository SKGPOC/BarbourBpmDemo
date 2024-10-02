package com.barbour.bpm.demo.controller;


import com.barbour.bpm.demo.bo.TaskRepresentation;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("/flowable/v1")
public class FlowableController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @PostMapping(value="/process")
    public void startProcessInstance() {
        runtimeService.startProcessInstanceByKey("babourpoc");
    }

    @GetMapping(value="/tasks")
    public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("legal_register_task_assignees").list();
        return tasks.stream().map(task -> new TaskRepresentation(task.getId(), task.getName())).collect(Collectors.toList());
    }
    @PostMapping(value="/assign/{taskId}/{assignee}")
    public void assignTask(@RequestParam String taskId, @RequestParam String assignee) {
        taskService.claim(taskId, assignee);
    }
    @PostMapping(value="/tasks/{taskId}")
    public void completeTask(@RequestParam String taskId) {
        taskService.complete(taskId);
    }


}
