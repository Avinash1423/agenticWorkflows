package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvaluatorOptimizer {

    ChatClient chatClient;
    public record evaluatorResponse(String result,String feedback){}

    @Autowired
    public EvaluatorOptimizer(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String runEvaluatorOptimizer(String userInput){

        String context="";

        int attempt=1;

        while (true){
            System.out.println("Attempt: "+   attempt++);

         String taskPrompt=context +"\nTask:  "+ userInput;
         String llmOutput=chatClient.prompt(taskPrompt).call().content();

         System.out.println(llmOutput);

         String evaluatorPrompt=
                  """
                  Evaluate how well this task is performed : {userInput} .
                 
                  This is the task output:{llmOutput}
                 
                  You must Give a result and a feedback.
                  
                  The result must be only either one of  PASS or FAIL.
                  
                  The feedback can be something to improve in the next iteration.
             
                  Give your response in the following JSON format:
                 
                  \\{
                    "result": "...",
                    "feedback":"..." 
                  \\}
               
                  """;

            evaluatorResponse evaluatorPromptResponse=chatClient.prompt().user(u->u.text(evaluatorPrompt).param("userInput",userInput).param("llmOutput",llmOutput)).call().entity(evaluatorResponse.class);

            System.out.println("----------------");
            System.out.println(evaluatorPromptResponse.result());
             System.out.println(evaluatorPromptResponse.feedback());
            System.out.println("----------------");

             if(evaluatorPromptResponse.result.equals("PASS")){

              return llmOutput;

             }

             else{

                 context=evaluatorPromptResponse.feedback();


             }



        }

    }
}
