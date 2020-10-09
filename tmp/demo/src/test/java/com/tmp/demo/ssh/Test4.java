package com.tmp.demo.ssh;

import java.util.*;

public class Test4 {

    public static void main(String[] args) {

        /**
         *            --> 6
         * 3&4 --> 2
         *            --> 1
         *         5
         */

        Digraph digraph = new Digraph();
        Task task1 = new Task(1L, "task1", 0);
        Task task2 = new Task(2L, "task2", 0);
        Task task3 = new Task(3L, "task3", 0);
        Task task4 = new Task(4L, "task4", 0);
        Task task5 = new Task(5L, "task5", 0);
        Task task6 = new Task(6L, "task6", 0);

        digraph.addTask(task1);
        digraph.addTask(task2);
        digraph.addTask(task3);
        digraph.addTask(task4);
        digraph.addTask(task5);
        digraph.addTask(task6);

        digraph.addEdge(task1, task2);
        digraph.addEdge(task1, task5);
        digraph.addEdge(task6, task2);
        digraph.addEdge(task2, task3);
        digraph.addEdge(task2, task4);
        Scheduler scheduler = new Scheduler();
        scheduler.schedule(digraph);
    }


    public static class Scheduler {
        // 遍历任务集合，找出待执行的任务集合，放到一个List中，再串行执行（若考虑性能，可优化为并行执行）。若List为空，说明所有任务都已执行，则这一次任务调度结束。
        public void schedule(Digraph digraph) {
            while (true) {
                List<Task> todo = new ArrayList<Task>();
                for (Task task : digraph.getTasks()) {
                    if (!task.hasExecuted()) {
                        Set<Task> prevs = digraph.getMap().get(task);
                        if (prevs != null && !prevs.isEmpty()) {
                            boolean toAdd = true;
                            for (Task task1 : prevs) {
                                if (!task1.hasExecuted()) {
                                    toAdd = false;
                                    break;
                                }
                            }
                            if (toAdd) {
                                todo.add(task);
                            }
                        } else {
                            todo.add(task);
                        }
                    }
                }
                if (!todo.isEmpty()) {
                    for (Task task : todo) {
                        if (!task.execute()) {
                            throw new RuntimeException();
                        }
                    }
                } else {
                    break;
                }
            }
        }

    }

    // 任务图
    public static class Digraph {
        private Set<Task> tasks;            // 顶点集合，也就是任务集合
        private Map<Task, Set<Task>> map;   // 任务依赖关系集合。key是一个任务，value是它的前置任务集合。一个任务执行的前提是它在map中没有以它作为key的entry，或者是它的前置任务集合中的任务都是已执行的状态。

        public Digraph() {
            this.tasks = new HashSet<>();
            this.map = new HashMap<>();
        }

        public void addEdge(Task task, Task prev) {
            if (!tasks.contains(task) || !tasks.contains(prev)) {
                throw new IllegalArgumentException();
            }
            Set<Task> prevs = map.get(task);
            if (prevs == null) {
                prevs = new HashSet<Task>();
                map.put(task, prevs);
            }
            if (prevs.contains(prev)) {
                throw new IllegalArgumentException();
            }
            prevs.add(prev);
        }

        public void addTask(Task task) {
            if (tasks.contains(task)) {
                throw new IllegalArgumentException();
            }
            tasks.add(task);
        }

        public void remove(Task task) {
            if (!tasks.contains(task)) {
                return;
            }
            if (map.containsKey(task)) {
                map.remove(task);
            }
            for (Set<Task> set : map.values()) {
                if (set.contains(task)) {
                    set.remove(task);
                }
            }
        }

        public Set<Task> getTasks() {
            return tasks;
        }

        public void setTasks(Set<Task> tasks) {
            this.tasks = tasks;
        }

        public Map<Task, Set<Task>> getMap() {
            return map;
        }

        public void setMap(Map<Task, Set<Task>> map) {
            this.map = map;
        }
    }

    public static class Task implements Executor{
        private Long id;
        private String name;
        private int state;  // 0：未执行，1：已执行

        public Task(Long id, String name, int state) {
            this.id = id;
            this.name = name;
            this.state = state;
        }

        public boolean execute() {
            System.out.println("Task id: [" + id + "], " + "task name: [" + name +"] is running");
            state = 1;
            return true;
        }

        public boolean hasExecuted() {
            return state == 1;
        }
    }

    public interface Executor {
        boolean execute();
    }


}
