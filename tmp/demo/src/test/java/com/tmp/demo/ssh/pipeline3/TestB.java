package com.tmp.demo.ssh.pipeline3;


import java.util.*;

public class TestB {

    public static void main(String[] args) {

        List<Action> actionList = Arrays.asList(
                new Demo01Action(),
                new Demo02Action(),
                new Demo03Action()
        );
        System.out.println("pipeline : " +  actionList.toString());

        doProcess(actionList, new ActionContext(new HashMap<String, String>()));
    }

    public static void doProcess(List<Action> actionList, ActionContext context) {
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

            context.getContextData().put("Demo01Action", "1");
            System.out.println(context.getContextData().values());
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

            context.getContextData().put("Demo02Action", "2");
            System.out.println(context.getContextData().values());

            throw new RuntimeException("Demo02Action Error");
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

            context.getContextData().put("Demo03Action", "3");
            System.out.println(context.getContextData().values());
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
