package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChainWorkflow {

    private static final String[] DEFAULT_SYSTEM_PROMPTS = {

            // Step 1
            """
					Extract only the numerical values and their associated metrics from the text.
					Format each as'value: metric' on a new line.
					Example format:
					92: customer satisfaction
					45%: revenue growth""",

            // Step 2
            """
					Convert all numerical values to percentages where possible.
					If not a percentage or points, convert to decimal (e.g., 92 points -> 92%).
					Keep one number per line.
					Example format:
					92%: customer satisfaction
					45%: revenue growth""",

            // Step 3
            """
					Sort all lines in descending order by numerical value.
					Keep the format 'value: metric' on each line.
					Example:
					92%: customer satisfaction
					87%: employee satisfaction""",

            // Step 4
            """
					Format the sorted data as a markdown table with columns:
					| Metric | Value |
					|:--|--:|
					| Customer Satisfaction | 92% | """
    };


     ChatClient chatClient;
    String[] systemPrompts;

    @Autowired
    public ChainWorkflow(ChatClient chatClient) {

        this.chatClient=chatClient;
        this.systemPrompts=DEFAULT_SYSTEM_PROMPTS;
    }


    public String chain(String userInput) {

        String input = userInput;
        int promtNumber=1;

        for (String prompt : systemPrompts) {



            System.out.println("--------------------------------------------");
            System.out.println(promtNumber +" - " +prompt);
            System.out.println("--------------------------------------------");


            String request=String.format("{%s}\n {%s}",prompt,input);

            String response=chatClient.prompt().user(request).call().content();

            System.out.println(response);


            input=response;

            promtNumber++;

        }


            return input;

    }

}

