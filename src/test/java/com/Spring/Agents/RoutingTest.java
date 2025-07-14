package com.Spring.Agents;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoutingTest {

    @Mock
    ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    ChatClient.CallResponseSpec responseSpec;

    @Mock
    ChatClient chatClient ;

    @InjectMocks
    Routing routing;

    @Test
    void determineRouteShouldSelectTheCorrectRoute() {

               String input="I was Overcharged";
               Iterable <String> section= List.of("Billing","technical");

               String sectionList=String.join(",",section);
               String inputPrompt=String.format("Carefully Analyze this input : ' %s ' and determine the most suitable section from : %s. Return ONLY the Category name." ,input,sectionList);

               when(chatClient.prompt(inputPrompt)).thenReturn(requestSpec);
               when(requestSpec.call()).thenReturn(responseSpec);
               when(responseSpec.content()).thenReturn("Billing");

               String result=routing.determineRoute(input,section);

               assertEquals("Billing",result);
               verify(chatClient).prompt(inputPrompt);

    }

    @Test
    void processRouteMustGiveCorrectRoute() {


        String input="I was Overcharged";

        Map<String,String> routeProcess=Map.of("billing",
                """
                        You are a billing support specialist. Follow these guidelines:
                        1. Always start with "Billing Support Response:"
                        2. First acknowledge the specific billing issue
                        3. Explain any charges or discrepancies clearly
                        4. List concrete next steps with timeline
                        5. End with payment options if relevant

                        Keep responses professional but friendly.

                        Input: """,

                "technical",
                """
                        You are a technical support engineer. Follow these guidelines:
                        1. Always start with "Technical Support Response:"
                        2. List exact steps to resolve the issue
                        3. Include system requirements if relevant
                        4. Provide workarounds for common problems
                        5. End with escalation path if needed

                        Use clear, numbered steps and technical details.
                        
                         Input:"""


        );

//        Routing routing = spy(new Routing(chatClient));
//
//        doReturn("billing").when(routing).determineRoute(anyString(), any());


        Routing routingSpy =spy(new Routing(chatClient));
        doReturn("billing").when(routingSpy).determineRoute(anyString(),any());




      when(chatClient.prompt(contains("Billing Support Response"))).thenReturn(requestSpec);
      when(requestSpec.call()).thenReturn(responseSpec);
      when(responseSpec.content()).thenReturn("This is working");//llm unpredictable output expected

        String result=routing.processRoute(input,routeProcess);

        assertEquals("This is working",result);






    }
}