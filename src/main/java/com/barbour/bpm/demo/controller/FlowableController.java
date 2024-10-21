package com.barbour.bpm.demo.controller;


import com.barbour.bpm.demo.bo.TaskRepresentation;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController("springManagedFlowableController")
@RequestMapping("/flowable/v1")
public class FlowableController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    @PostMapping(value="/process")
    public void startProcessInstance() {
        runtimeService.startProcessInstanceByKey("babourpoc");
    }

    @GetMapping(value="/tasks")
    public List<TaskRepresentation> getTasks() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("legal_register_task_assignees").list();
        System.out.println("tasks = " + tasks);
        return tasks.stream().map(task -> new TaskRepresentation(task.getId(), task.getName())).collect(Collectors.toList());
    }
    @PostMapping(value="/assign/{taskId}/{assignee}")
    public void assignTask(@PathVariable String taskId, @PathVariable String assignee) {
        taskService.claim(taskId, assignee);
    }
    @PostMapping(value="/tasks/approve/{taskId}")
    public void completeTask(@PathVariable String taskId) {
        taskService.setVariable(taskId, "nextStep", "Approved");
        taskService.complete(taskId);
    }
    @PostMapping(value="/tasks/update/{taskId}")
    public void updateTask(@PathVariable String taskId,@RequestBody String comment) {
        taskService.setVariable(taskId, "nextStep", "updated");
        taskService.addComment(taskId, taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId(), comment);
        taskService.complete(taskId);
    }


    @GetMapping(value="/history")
    public List<HistoricProcessInstance>  getHistory() {
        List<HistoricProcessInstance> history=historyService.createHistoricProcessInstanceQuery().processDefinitionKey("babourpoc").list();
        return history;
    }

    @GetMapping(value="/history/{processInstanceId}")
    public List<HistoricActivityInstance> getProcessHistory(@PathVariable String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
    }


}
