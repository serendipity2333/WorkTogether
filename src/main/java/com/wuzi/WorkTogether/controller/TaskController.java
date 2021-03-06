package com.wuzi.WorkTogether.controller;

import com.wuzi.WorkTogether.domain.SubTask;
import com.wuzi.WorkTogether.domain.Task;
import com.wuzi.WorkTogether.domain.Team;
import com.wuzi.WorkTogether.domain.dto.SubTaskDto;
import com.wuzi.WorkTogether.domain.dto.TaskDto;
import com.wuzi.WorkTogether.domain.dto.UserDto;
import com.wuzi.WorkTogether.service.TaskService;
import com.wuzi.WorkTogether.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 施武轩
 * @version 1.0
 * @date 2020/11/8 14:35
 * @lastEditor
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    private final List<SubTask> tempSubTaskList = new ArrayList<>();

    @Resource
    private TeamService teamService;

    @Resource
    private TaskService taskService;


    @RequestMapping("/newTask/{teamId}")
    public ModelAndView  newTask(ModelAndView modelAndView, @PathVariable Integer teamId){
        tempSubTaskList.clear();
        RedirectView redirectView = new RedirectView("/WorkTogether/task/gotoAddTask/{teamId}");
        modelAndView.setView(redirectView);
        modelAndView.addObject("teamId", teamId);
        return modelAndView;
    }

    @RequestMapping("/myTask/{userId}")
    public String userAllTask(Model model, @PathVariable Integer userId){
        List<TaskDto> tasks = taskService.queryAllTaskByUser(userId);
        model.addAttribute("tasks",tasks);
        return "myTask";
    }

//    @RequestMapping("/gotoAddNewTask")
//    public String gotoAddTask(){
//        tempSubTaskList.clear();
//        return "addTask";
//    }

    @RequestMapping("/gotoAddTask/{teamId}")
    public String gotoAddTask(Model model,@PathVariable Integer teamId){
        model.addAttribute("tempList",tempSubTaskList);
        Team team = teamService.findTeamById(teamId);
        List<UserDto> members = teamService.showTeamMember(teamId);
        model.addAttribute("members",members);
        model.addAttribute("team",team);
        return "addTask";
    }

    @RequestMapping("/addSubTask")
    public String addSubTask(Model model,String content, Integer weight){
        SubTask subTask = new SubTask();
        subTask.setSubTaskId(tempSubTaskList.size()+1);
        System.out.println(tempSubTaskList.toString());
        subTask.setContent(content);
        subTask.setWeight(weight);
        tempSubTaskList.add(subTask);
        return "addTask";
    }

    @RequestMapping("/addTask")
    public ModelAndView addTask(ModelAndView modelAndView, HttpServletRequest request,Task task){
        int teamId = Integer.parseInt(request.getParameter("teamId"));
        taskService.addTask(task);
        int totalWeight = 0;
        for (SubTask t:tempSubTaskList) {
            totalWeight+=t.getWeight();
        }

        for (SubTask t:tempSubTaskList){
            if (totalWeight!=100){
                t.setWeight((t.getWeight()*100)/totalWeight);
            }
            t.setTaskId(task.getTaskId());
            taskService.addSubTask(t);
        }
        tempSubTaskList.clear();
        //TODO 返回团队界面
        RedirectView redirectView = new RedirectView("/WorkTogether/team/teamDetail/{teamId}");
        modelAndView.setView(redirectView);
        modelAndView.addObject("teamId", teamId);
        return modelAndView;
    }

    @RequestMapping("/taskDetail/{taskId}")
    public String gotoTaskDetail(Model model,@PathVariable Integer taskId){
        List<SubTaskDto> subTasks = taskService.querySubTask(taskId);
        model.addAttribute("subTasks",subTasks);
        model.addAttribute("taskName",taskService.queryTaskName(taskId));
        model.addAttribute("progress",taskService.queryTaskProgress(taskId));
        return "taskDetail";
    }

//    @RequestMapping("/updateTask/{userId}/{progress}")
//    public String updateTask(@PathVariable Integer userId,@PathVariable Integer progress){
//        Task task = new Task();
//        task.setMemberId(userId);
//        task.setTaskProgress(progress);
//        taskService.addTask(task);
//        return "redirect:myTask";
//    }


    @RequestMapping("/myUrgentTask/{userId}")
    public String showUrgentTask(Model model,@PathVariable Integer userId){
        List<TaskDto> tasks = taskService.queryAllTaskByUser(userId);
        tasks.removeIf(t -> t.getPriority() != 2);
        model.addAttribute("tasks",tasks);
        return "myTask";
    }

    @RequestMapping("/queryTask/{userId}")
    public String queryTaskByUser(Model model,@PathVariable Integer userId,@RequestParam String keyword){
        List<TaskDto> tasks = taskService.queryTaskByKeyword(userId,keyword);
        model.addAttribute("tasks",tasks);
        return "myTask";
    }

    @RequestMapping("/completeSubTask/{taskId}/{subTaskId}")
    public String completeSubTask(Model model, @PathVariable Integer taskId, @PathVariable Integer subTaskId){
        List<SubTaskDto> subTasks = taskService.completeTask(taskId,subTaskId);
        model.addAttribute("subTasks",subTasks);
        model.addAttribute("taskName",taskService.queryTaskName(taskId));
        model.addAttribute("progress",taskService.queryTaskProgress(taskId));
        return "taskDetail";
    }

    @RequestMapping("/todoList")
    public String todoList(){
        return "calendar";
    }
}



