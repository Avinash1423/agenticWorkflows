package com.Spring.Agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RunClass implements CommandLineRunner {


    ChatClient chatClient;
    ChainWorkflow chainWorkflow;
    Sectioning sectioning; //parallel workflow
    Routing routing;
    OrchestratorWorkflow orchestratorWorkflow;
    EvaluatorOptimizer evaluatorOptimizer;


    @Autowired
    public RunClass(ChatClient chatClient, ChainWorkflow chainWorkflow,Sectioning sectioning,    Routing routing, OrchestratorWorkflow orchestratorWorkflow,EvaluatorOptimizer evaluatorOptimizer) {
        this.chatClient = chatClient;
        this.chainWorkflow= chainWorkflow;
        this.sectioning=sectioning;
        this.routing=routing;
        this.orchestratorWorkflow=orchestratorWorkflow;
        this.evaluatorOptimizer=evaluatorOptimizer;
    }


    @Override
    public void run(String... args) throws Exception {

        /// Chain WorkFlow
        String report = """
			Q3 Performance Summary:
			Our customer satisfaction score rose to 92 points this quarter.
			Revenue grew by 45% compared to last year.
			Market share is now at 23% in our primary market.
			Customer churn decreased to 5% from 8%.
			New user acquisition cost is $43 per user.
			Product adoption rate increased to 78%.
			Employee satisfaction is at 87 points.
			Operating margin improved to 34%.
			""";
      //  String finaOutput =chainWorkflow.chain(report); System.out.println(finaOutput);
        
        /// Chain WorkFlow

        //*****************//

        /// Sectioning ,parallel workflow


        String prompt="Analyze how market changes will impact this stakeholder group.Provide specific impacts and recommended actions.Format with clear sections and priorities.";

        List<String> inputs=List.of(
                """
                Customers:
                - Price sensitive
                - Want better tech
                - Environmental concerns
                """,

                """
                Employees:
                - Job security worries
                - Need new skills
                - Want clear direction
                """,

                """
                Investors:
                - Expect growth
                - Want cost control
                - Risk concerns
                """,

                """
                Suppliers:
                - Capacity constraints
                - Price pressures
                - Tech transitions
                """
        );


//       List<String> sectionOutput=sectioning.section(prompt,inputs);

//       System.out.println(sectionOutput);

        /// Sectioning ,parallel workflow

        //*****************//


/// Routing
        Map<String, String> supportRoutes = Map.of("billing",
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

                        Input: """,

                "account",
                """
                        You are an account security specialist. Follow these guidelines:
                        1. Always start with "Account Support Response:"
                        2. Prioritize account security and verification
                        3. Provide clear steps for account recovery/changes
                        4. Include security tips and warnings
                        5. Set clear expectations for resolution time

                        Maintain a serious, security-focused tone.

                        Input: """,

                "product",
                """
                        You are a product specialist. Follow these guidelines:
                        1. Always start with "Product Support Response:"
                        2. Focus on feature education and best practices
                        3. Include specific examples of usage
                        4. Link to relevant documentation sections
                        5. Suggest related features that might help

                        Be educational and encouraging in tone.

                        Input: """);

        List<String> tickets = List.of(
                """
                        Subject: Can't access my account
                        Message: Hi, I've been trying to log in for the past hour but keep getting an 'invalid password' error.
                        I'm sure I'm using the right password. Can you help me regain access? This is urgent as I need to
                        submit a report by end of day.
                        - John""",

                """
                        Subject: Unexpected charge on my card
                        Message: Hello, I just noticed a charge of .99 on my credit card from your company, but I thought
                        I was on the .99 plan. Can you explain this charge and adjust it if it's a mistake?
                        Thanks,
                        Sarah""",

                """
                        Subject: How to export data?
                        Message: I need to export all my project data to Excel. I've looked through the docs but can't
                        figure out how to do a bulk export. Is this possible? If so, could you walk me through the steps?
                        Best regards,
                        Mike""");


//        for (String ticket:tickets) {
//
//           String result= routing.processRoute(ticket, supportRoutes);
//
//           System.out.println(result);
//        }


/// Routing

        //*****************//

        ///         Orchestrator Workflow

         String userInput="Write a product description for a new eco-friendly water bottle";
               System.out.println( orchestratorWorkflow.OrchestratorWorks(userInput));

        ///         Orchestrator Workflow


        //*****************//


/// Evaluator Optimizer

//        String userInput="Create a Java function that reverses a String.";
//        evaluatorOptimizer.runEvaluatorOptimizer(userInput);


        /// Evaluator Optimizer

        //*****************//



    }
}
