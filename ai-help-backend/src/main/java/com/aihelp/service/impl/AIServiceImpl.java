package com.aihelp.service.impl;

import com.aihelp.ai.AIProvider;
import com.aihelp.ai.TongyiProvider;
import com.aihelp.dto.request.AIRequest;
import com.aihelp.dto.response.AIResponse;
import com.aihelp.dto.response.TaskResponse;
import com.aihelp.entity.Task;
import com.aihelp.mapper.TaskMapper;
import com.aihelp.service.IAIService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIServiceImpl implements IAIService {

    private final TaskMapper taskMapper;
    private final AIProvider aiProvider;

    @Override
    public AIResponse chat(AIRequest request) {
        String content = aiProvider.chat(request.getPrompt());
        return AIResponse.builder()
                .content(content)
                .type("chat")
                .build();
    }

    @Override
    public AIResponse decomposeTask(Long userId, String taskTitle, String taskDescription) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 0);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildDecomposePrompt(taskTitle, taskDescription, tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("decompose")
                .build();
    }

    @Override
    public AIResponse sortPriority(Long userId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 0);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildSortPriorityPrompt(tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("priority_sort")
                .build();
    }

    @Override
    public AIResponse generateDailyPlan(Long userId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 0);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildDailyPlanPrompt(tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("daily_plan")
                .build();
    }

    @Override
    public AIResponse generateWeeklyPlan(Long userId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 0);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildWeeklyPlanPrompt(tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("weekly_plan")
                .build();
    }

    @Override
    public AIResponse generateDailyReport(Long userId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 1);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildDailyReportPrompt(tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("daily_report")
                .build();
    }

    @Override
    public AIResponse generateWeeklyReport(Long userId) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getUserId, userId)
                .eq(Task::getStatus, 1);
        List<Task> tasks = taskMapper.selectList(wrapper);

        String prompt = buildWeeklyReportPrompt(tasks);
        String content = aiProvider.chat(prompt);

        return AIResponse.builder()
                .content(content)
                .type("weekly_report")
                .build();
    }

    private String buildDecomposePrompt(String taskTitle, String taskDescription, List<Task> existingTasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个任务管理专家。请将以下复杂任务拆解为可执行的小任务。\n\n");
        sb.append("拆解要求：\n");
        sb.append("1. 拆解为5-10个具体的子任务\n");
        sb.append("2. 每个子任务尽量在2小时内可以完成\n");
        sb.append("3. 按照合理的执行顺序排列\n");
        sb.append("4. 直接用中文输出子任务列表，格式如下：\n");
        sb.append("   一级标题（用一、二、三...）\n");
        sb.append("   二级内容（用1. 2. 3...）\n");
        sb.append("   每条包含：任务名称、建议优先级（高/中/低）、建议时长\n\n");
        sb.append("主任务：").append(taskTitle).append("\n");
        if (taskDescription != null && !taskDescription.isEmpty()) {
            sb.append("任务描述：").append(taskDescription).append("\n");
        }
        if (!existingTasks.isEmpty()) {
            sb.append("\n当前已有的相关任务：\n");
            existingTasks.forEach(t -> sb.append("- ").append(t.getTitle()).append("\n"));
        }
        return sb.toString();
    }

    private String buildSortPriorityPrompt(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个任务管理专家。请根据以下任务的重要性、紧急程度和截止时间，对任务进行优先级排序。\n\n");
        sb.append("排序要求：\n");
        sb.append("1. 截止时间最紧急的优先\n");
        sb.append("2. 重要程度高的优先\n");
        sb.append("3. 依赖关系考虑在内\n");
        sb.append("4. 直接用中文给出排序结果和建议，格式清晰易读\n\n");
        sb.append("当前待办任务：\n");
        if (tasks.isEmpty()) {
            sb.append("暂无待办任务\n");
        } else {
            for (Task task : tasks) {
                sb.append("- ").append(task.getTitle());
                sb.append(" | 优先级:").append(translatePriority(task.getPriority()));
                sb.append(" | 截止:").append(task.getDeadline() != null ? task.getDeadline() : "未设置");
                sb.append("\n");
            }
        }
        sb.append("\n请给出排序建议和理由，全部使用中文回答。");
        return sb.toString();
    }

    private String translatePriority(String priority) {
        if (priority == null) return "未设置";
        switch (priority) {
            case "high": return "高";
            case "middle": return "中";
            case "low": return "低";
            default: return priority;
        }
    }

    private String buildDailyPlanPrompt(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个任务管理专家。请根据以下待办任务，制定今日工作计划。\n\n");
        sb.append("制定要求：\n");
        sb.append("1. 合理安排任务顺序和时间\n");
        sb.append("2. 优先处理紧急且重要的任务\n");
        sb.append("3. 每天选择3-5个核心任务即可\n");
        sb.append("4. 用中文详细输出，包含时间安排和具体步骤\n\n");
        sb.append("待办任务：\n");
        if (tasks.isEmpty()) {
            sb.append("暂无待办任务\n");
        } else {
            for (Task task : tasks) {
                sb.append("- ").append(task.getTitle());
                sb.append(" | 优先级:").append(translatePriority(task.getPriority()));
                sb.append(" | 截止:").append(task.getDeadline() != null ? task.getDeadline() : "未设置");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String buildWeeklyPlanPrompt(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个任务管理专家。请根据以下待办任务，制定本周工作计划。\n\n");
        sb.append("制定要求：\n");
        sb.append("1. 将任务分配到本周各天\n");
        sb.append("2. 每天设置2-4个核心任务\n");
        sb.append("3. 周末预留一定缓冲时间\n");
        sb.append("4. 用中文详细输出，包含每天的具体安排\n\n");
        sb.append("待办任务：\n");
        if (tasks.isEmpty()) {
            sb.append("暂无待办任务\n");
        } else {
            for (Task task : tasks) {
                sb.append("- ").append(task.getTitle());
                sb.append(" | 优先级:").append(translatePriority(task.getPriority()));
                sb.append(" | 截止:").append(task.getDeadline() != null ? task.getDeadline() : "未设置");
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String buildDailyReportPrompt(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个工作总结专家。请根据今日完成的任务，生成工作总结。\n\n");
        sb.append("总结要求：\n");
        sb.append("1. 总结今日完成的主要工作\n");
        sb.append("2. 列出存在的不足和改进方向\n");
        sb.append("3. 语言简洁有条理，全部使用中文\n\n");
        sb.append("今日完成的任务：\n");
        if (tasks.isEmpty()) {
            sb.append("今日暂无完成的任务\n");
        } else {
            for (Task task : tasks) {
                sb.append("- ").append(task.getTitle());
                if (task.getCompletedTime() != null) {
                    sb.append(" | 完成时间:").append(task.getCompletedTime());
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String buildWeeklyReportPrompt(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个工作总结专家。请根据本周完成的任务，生成周报。\n\n");
        sb.append("周报要求：\n");
        sb.append("1. 总结本周完成的主要工作\n");
        sb.append("2. 统计本周任务完成情况\n");
        sb.append("3. 分析存在的问题和下周改进计划\n");
        sb.append("4. 语言专业有条理，全部使用中文\n\n");
        sb.append("本周完成的任务：\n");
        if (tasks.isEmpty()) {
            sb.append("本周暂无完成的任务\n");
        } else {
            for (Task task : tasks) {
                sb.append("- ").append(task.getTitle());
                if (task.getCompletedTime() != null) {
                    sb.append(" | 完成时间:").append(task.getCompletedTime());
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
