package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Sectioning {

    ChatClient chatClient;

    @Autowired
    public Sectioning(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public List<String> section(String prompt, List<String> inputs){

        return inputs.parallelStream().map(input->chatClient.prompt(prompt+"\nInput:"+input).call().content()).toList();



    }
}
