//package com.xuxueli.demo.graphql;
//
//import graphql.GraphQL;
//import graphql.Scalars;
//import graphql.schema.*;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import static graphql.schema.GraphQLArgument.newArgument;
//import static graphql.schema.GraphQLObjectType.newObject;
//
///**
// * GraphQL
// *
// *  优点：
// *      1、聚合请求，减少请求次数；
// *      2、动态定制响应数据格式，调用方可以明确知道返回数据的结构；
// *
// *  缺点：
// *      1、聚合请求时，Server端串行，耗时不可控；
// *      2、由请求方定义数据结构，对前端要求较高；
// *
// *        JDK8:<source>1.7</source>
// */
//public class GraphQLDemo2 {
//
//    public static void main(String[] args) {
//
//        /*
//         * Query example:
//         *
//         * String query1 = "{pageUserList(page:2,size:5,name:\"john\") {id,name}}";
//         * String query2 = "{loadUser(id:6) {id,name}}";
//         * String query3 = "{loadUser(id:6) {id,name},pageUserList(page:2,size:5,name:\"john\") {id,name}}"
//         * ;
//         */
//
//        String query = "{loadUser(id:6) {id,name},pageUserList(page:2,size:5,name:\"john\") {id,name}}";
//
//        long start = System.currentTimeMillis();
//        Map<String, Object> result = graphQL.execute(query).getData();
//        long end = System.currentTimeMillis();
//        System.out.println("cost:" + (end - start));
//
//        System.out.println(result);
//    }
//
//    private static GraphQL graphQL;
//    private static GraphQLOutputType outputType;
//
//    static {
//
//        // OutputType（响应数据对象）
//        outputType = newObject()
//                .name("User")
//                .field(GraphQLFieldDefinition.newFieldDefinition().name("id").type(Scalars.GraphQLLong).build())
//                .field(GraphQLFieldDefinition.newFieldDefinition().name("age").type(Scalars.GraphQLShort).build())
//                .field(GraphQLFieldDefinition.newFieldDefinition().name("name").type(Scalars.GraphQLString).build())
//                .build();
//
//        // ObjectType (查询数据对象)
//        GraphQLObjectType objectType = newObject()
//                .name("GraphQuery")
//                .field(pageUserListField())
//                .field(loadUserField())
//                .build();
//
//        // Schema：User
//        GraphQLSchema schema = GraphQLSchema.newSchema().query(objectType).build();
//
//        // Main GraphQL
//        graphQL = GraphQL.newGraphQL(schema).build();
//    }
//
//
//    /**
//     * FieldDefinition：Load 用户
//     */
//    private static GraphQLFieldDefinition loadUserField() {
//        return GraphQLFieldDefinition.newFieldDefinition().name("loadUser")
//                .argument(newArgument().name("id").type(Scalars.GraphQLInt).build())
//                .type(outputType)
//                .dataFetcher(new DataFetcher<User>(){
//                    @Override
//                    public User get(DataFetchingEnvironment environment) {
//
//                        System.out.println( System.currentTimeMillis() + ": invoke loadUser...");
//
//                        // 获取查询参数
//                        int id = environment.getArgument("id");
//
//                        // 执行查询, 模拟查询
//                        User user = new User();
//                        user.setId(id);
//                        user.setName("Name_" + id);
//
//                        try {
//                            TimeUnit.SECONDS.sleep(3);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        return user;
//                    }
//                }).build();
//    }
//
//    /**
//     * FieldDefinition：分页查询
//     */
//    private static GraphQLFieldDefinition pageUserListField() {
//        return GraphQLFieldDefinition.newFieldDefinition()
//                .name("pageUserList")
//                .argument(newArgument().name("page").type(Scalars.GraphQLInt).build())
//                .argument(newArgument().name("size").type(Scalars.GraphQLInt).build())
//                .argument(newArgument().name("name").type(Scalars.GraphQLString).build())
//                .type(new GraphQLList(outputType))
//                .dataFetcher(new DataFetcher<List<User>>(){
//                    @Override
//                    public List<User> get(DataFetchingEnvironment environment) {
//
//                        System.out.println( System.currentTimeMillis() + ": invoke pageUserList...");
//
//                        // 获取查询参数
//                        int page = environment.getArgument("page");
//                        int size = environment.getArgument("size");
//                        String name = environment.getArgument("name");
//
//                        // 执行查询, 模拟查询
//                        List<User> list = new ArrayList<>(size);
//                        for (int i = 0; i < size; i++) {
//                            User user = new User();
//                            user.setId(i);
//                            user.setAge((short)i );
//                            user.setName("Name_" + i);
//                            list.add(user);
//                        }
//
//                        try {
//                            TimeUnit.SECONDS.sleep(3);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        return list;
//
//                    }
//                }).build();
//    }
//
//
//    // model
//    public static class User implements Serializable {
//
//        private long id;
//        private String name;
//        private short age;
//
//        public long getId() {
//            return id;
//        }
//        public void setId(long id) {
//            this.id = id;
//        }
//        public String getName() {
//            return name;
//        }
//        public void setName(String name) {
//            this.name = name;
//        }
//        public short getAge() {
//            return age;
//        }
//        public void setAge(short age) {
//            this.age = age;
//        }
//
//    }
//
//}
