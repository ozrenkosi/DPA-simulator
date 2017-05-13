//
// Created by ozrenkosi on 15.04.2017..
//

import java.util.*;

public class SimPa {

    private static List<String> inputStrings = new ArrayList<>();
    private static List<String> currentInputString = new ArrayList<>();
    private static List<String> acceptedStates = new ArrayList<>();
    private static String initialState;
    private static String initialStackSymbol;
    private static Map<List<String>, List<String>> transitionFunction = new HashMap<>();
    private static Stack<String> stack = new Stack<>();

    public static void main(String[] args) {

        dataInput();

        for (int i = 0; i < inputStrings.size(); i++) {
            currentInputString.addAll(Arrays.asList(inputStrings.get(i).split(",")));
            runDpaSimulation();
            currentInputString.clear();
        }

    }

    private static void dataInput() {
        String[] temporaryInput;

        Scanner reader = new Scanner(System.in);

        temporaryInput = reader.nextLine().split("\\|");
        inputStrings.addAll(Arrays.asList(temporaryInput));

        reader.nextLine();
        reader.nextLine();
        reader.nextLine();

        temporaryInput = reader.nextLine().split(",");
        acceptedStates.addAll(Arrays.asList(temporaryInput));

        initialState = reader.nextLine();

        initialStackSymbol = reader.nextLine();


        while (reader.hasNextLine()) {
            temporaryInput = reader.nextLine().split("->");

            if (temporaryInput[0].equals("")) {
                break;
            }

            List<String> key = new ArrayList<>();
            List<String> value = new ArrayList<>();

            key.addAll(Arrays.asList(temporaryInput[0].split(",")));
            value.addAll(Arrays.asList(temporaryInput[1].split(",")));

            transitionFunction.put(key, value);
        }

        reader.close();
    }

    private static void runDpaSimulation() {
        List<String> key = new ArrayList<>();
        List<String> epsilonKey = new ArrayList<>();
        String pastState = initialState;

        pushOnStack(initialStackSymbol);
        printSimulationStep(initialState);

        for (int i = 0; i < currentInputString.size(); i++) {
            key.addAll(Arrays.asList(pastState, currentInputString.get(i), stack.pop()));
            epsilonKey.addAll(Arrays.asList(pastState, "$", key.get(2)));

            if (transitionFunction.containsKey(key)) {
                pushOnStack(transitionFunction.get(key).get(1));
                printSimulationStep(transitionFunction.get(key).get(0));

                pastState = transitionFunction.get(key).get(0);
            }
            else if (transitionFunction.containsKey(epsilonKey)) {
                pushOnStack(transitionFunction.get(epsilonKey).get(1));
                printSimulationStep(transitionFunction.get(epsilonKey).get(0));

                pastState = transitionFunction.get(epsilonKey).get(0);

                i--;
            }
            else {
                System.out.println("fail|0");
                return;
            }

            if (stack.isEmpty() && i != currentInputString.size()-1) {
                System.out.println("fail|0");
                return;
            }

            key.clear();
            epsilonKey.clear();
        }

        if (acceptedStates.contains(pastState)) {
            System.out.println("1");
        }
        else {
            runEpsilonTransitions(pastState);
        }

        stack.clear();
    }

    private static void printStack() {
        if (stack.isEmpty()) {
            System.out.print("$");
            return;
        }

        String stackFormatted = stack.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")
                .trim();

        StringBuilder stackFormattedReversed = new StringBuilder();

        stackFormattedReversed.append(stackFormatted);
        stackFormattedReversed = stackFormattedReversed.reverse();

        System.out.print(stackFormattedReversed);
    }

    private static void printSimulationStep(String state) {
        System.out.print(state + "#");
        printStack();
        System.out.print("|");
    }

    private static void pushOnStack(String niz) {
        if (niz.equals("$")) {
            return;
        }
        for (int i = niz.length() - 1; i >= 0; i--) {
            stack.push(Character.toString(niz.charAt(i)));
        }
    }

    private static void runEpsilonTransitions(String pastState) {
        while (!stack.isEmpty()) {
            List<String> key = new ArrayList<>();

            key.addAll(Arrays.asList(pastState, "$", stack.pop()));

            if (!transitionFunction.containsKey(key)) {
                break;
            }

            pushOnStack(transitionFunction.get(key).get(1));
            printSimulationStep(transitionFunction.get(key).get(0));

            pastState = transitionFunction.get(key).get(0);

            if (acceptedStates.contains(pastState)) {
                System.out.println("1");
                return;
            }

            key.clear();
        }

        System.out.println("0");
    }

}