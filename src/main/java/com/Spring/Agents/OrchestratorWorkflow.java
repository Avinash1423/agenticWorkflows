package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrchestratorWorkflow {

    ChatClient chatClient;

    public record Task(String type,String description){}
    public record OrchestratorResponse(String analysis, List<Task> tasks){}
    public record FinalResponse(String analysis,List<String> workerResponse){};

    @Autowired
    public OrchestratorWorkflow(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String  OrchestratorWorks(String userInput){

        String OrchestratorPrompt= """
			Analyze this task and break it down into 2-3 distinct approaches:

			Task: {task}

			Return your response in this JSON format:
			\\{
			"analysis": "Explain your understanding of the task and which variations would be valuable.
			             Focus on how each approach serves different aspects of the task.",
			"tasks": [
				\\{
				"type": "formal",
				"description": "Write a precise, technical version that emphasizes specifications"
				\\},
				\\{
				"type": "conversational",
				"description": "Write an engaging, friendly version that connects with readers"
				\\}
			]
			\\}
			""";


        OrchestratorResponse orchestratorResponse=chatClient.prompt().user(
       u->u.text(OrchestratorPrompt).param("task",userInput)).call().entity(OrchestratorResponse.class);

		System.out.println("Analysis :" + orchestratorResponse.analysis());
		for(Task task:orchestratorResponse.tasks()){

//			System.out.println("------------------");
//			System.out.println("TYPE :" + task.type());
//			System.out.println("DESCRIPTION :" + task.description());
//			System.out.println("-------------------");
		}
        String workerPrompt= """
                
                Generate content for :
                
                Task:{userInput}
                Style:{taskType}
                Instructions:{taskDescription}
                
                """;

      List<String> WorkerResponses=orchestratorResponse.tasks().stream().map(task->chatClient.prompt().user(u->u.text(workerPrompt).param("userInput",userInput).param("taskType", task.type()).param("taskDescription",task.description())).call().content()).toList();

//	  for(String workerResponse:WorkerResponses) {
//		  System.out.println("------------------");
//		  System.out.println(workerResponse);
//		  System.out.println("-------------------");
//	  }

	  String finalCombination= """
	
			    Task:{userInput}
			 
			   For the above task use the information below to come up with a single polishes output.
			  
			   Analysis of the task:{analysis}
			 
			   Responses:{responses}
			 
			  """;

	  return chatClient.prompt().user(u->u.text(finalCombination).param("userInput",userInput).param("analysis",orchestratorResponse.analysis()).param("responses",String.join("\n",WorkerResponses))).call().content();

    }







}
