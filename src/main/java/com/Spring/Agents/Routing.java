package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Routing {

    ChatClient chatClient;
@Autowired
    public Routing(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String determineRoute(String input,Iterable<String> sections){

        String sectionList=String.join(",",sections);

        String determinePrompt=String.format("Carefully Analyze this input : ' %s ' and determine the most suitable section from : %s. Return ONLY the Category name." ,input,sectionList);

             String selectedSection= chatClient.prompt(determinePrompt).call().content();
        System.out.println("From determineRoute " + selectedSection );

        return selectedSection;





    }


    public String processRoute(String Input, Map<String,String> routeProcess){


        String selectedSection=determineRoute(Input, routeProcess.keySet());

        String promptAdvice=routeProcess.get(selectedSection);

        return chatClient.prompt(promptAdvice + "\nInput: "+Input).call().content();



    }



}
