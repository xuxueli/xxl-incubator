package com.tmp.demo.ssh.pipeline3;


import java.util.*;

public class TestB {

    public static void main(String[] args) {

        String actionConf = "1, 3, 2";

        doProcess(actionConf, new ActionContext(new HashMap<String, String>()));
    }

    private static Map<String, Action> actionMap = new HashMap<>();
    static {
        actionMap.put("1", new Demo01Action());
        actionMap.put("2", new Demo02Action());
        actionMap.put("3", new Demo03Action());
    }

    public static void doProcess(String actionConf, ActionContext context) {

        // parse
        List<Action> actionList = new ArrayList<>();
        List<String> actionConfList = Arrays.asList(actionConf.split(","));
        for (String actionName: actionConfList) {
            Action action = actionMap.get(actionName.trim());
            actionList.add(action);
        }

        // param
        List<Action> invokedActionList = new ArrayList<>();
        boolean processSuccess = true;

        // invoke
        for (Action action: actionList) {
            invokedActionList.add(action);

            // do invoke
            try {
                action.process(context);
            } catch (Exception e) {
                e.printStackTrace();

                processSuccess = false;
                break;
            }

        }

        // cancel
        if (!processSuccess && invokedActionList.size() > 0) {
            Collections.reverse(invokedActionList);
            for (Action action: invokedActionList) {
                try {
                    action.cancel(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static class Demo01Action extends Action {

        @Override
        public void process(ActionContext context) {
            System.out.println("Demo01Action process");

            /*context.getContextData().put("Demo01Action", "1");
            System.out.println(context.getContextData().values());*/
        }

        @Override
        public void cancel(ActionContext context) {
            System.out.println("Demo01Action cancel");
        }

    }

    public static class Demo02Action extends Action {

        @Override
        public void process(ActionContext context) {
            System.out.println("Demo02Action process");

            /*context.getContextData().put("Demo02Action", "2");
            System.out.println(context.getContextData().values());

            throw new RuntimeException("Demo02Action Error");*/
        }

        @Override
        public void cancel(ActionContext context) {
            System.out.println("Demo02Action cancel");
        }

    }

    public static class Demo03Action extends Action {

        @Override
        public void process(ActionContext context) {
            System.out.println("Demo03Action process");

            /*context.getContextData().put("Demo03Action", "3");
            System.out.println(context.getContextData().values());*/
        }

        @Override
        public void cancel(ActionContext context) {
            System.out.println("Demo03Action cancel");
        }

    }

    public static abstract class Action {

        public abstract void process(ActionContext context);

        public abstract void cancel(ActionContext context);

    }

    public static class ActionContext {

        public ActionContext(Map<String, String> contextData) {
            this.contextData = contextData;
        }

        private Map<String,String> contextData;

        public Map<String, String> getContextData() {
            return contextData;
        }
        public void setContextData(Map<String, String> contextData) {
            this.contextData = contextData;
        }

    }

}
