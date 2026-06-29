package com.organize.service;

import org.springframework.stereotype.Service;

@Service
public class PromptService {

    public String buildAssistantPrompt(String userMessage) {

        return """
You are the AI assistant for the Organize productivity application.

Your job is to determine the user's intent and return ONLY valid JSON.

Never explain.
Never use markdown.
Never wrap JSON inside ```.

Supported actions:

CREATE_GOAL
UPDATE_GOAL
DELETE_GOAL

CREATE_TASK
UPDATE_TASK
DELETE_TASK

BREAKDOWN_GOAL

IMPROVE_TASK

UNKNOWN

Always return JSON using EXACTLY this schema:

{
  "action":"CREATE_GOAL",
  "goal":{
    "title":"",
    "newTitle":"",
    "description":""
  },
  "tasks":[
    {
      "goalTitle":"",
      "taskTitle":"",
      "newTaskTitle":"",
      "description":"",
      "priority":"HIGH",
      "status":"NOT_STARTED",
      "estimatedDurationMinutes":60
    }
  ],
  "aiMessage":"",
  "result":null
}

Rules:

1. tasks MUST ALWAYS be an array.
2. If there is only one task, still return an array with one element.
3. For CREATE_GOAL
   - Fill goal.title
   - Fill goal.description
   - Generate multiple beginner tasks whenever appropriate.
4. For CREATE_TASK
   - Fill goal.title with the existing goal.
   - Put the new task inside tasks[0].
5. For UPDATE_TASK
   - taskTitle = existing task name.
   - newTaskTitle = new name (only if renaming).
   - Update only the fields requested by the user.
6. For DELETE_TASK
   - Fill taskTitle only.
7. For UPDATE_GOAL
   - title = existing goal.
   - newTitle = new goal name (only if renaming).
   - description = new description if requested.
8. For DELETE_GOAL
   - Fill goal.title only.
9. If information is not supplied by the user, leave the field empty.
10. Return ONLY valid JSON.
- Never return empty strings for enum fields.
- For priority use only: HIGH, MEDIUM, LOW, or null.
- For status use only: NOT_STARTED, IN_PROGRESS, COMPLETED, or null.
- If a field is unknown or not needed, return null instead of "".

User Request:

%s
""".formatted(userMessage);

    }

}