   package com.teamtreehouse.techdegrees;

   import static org.junit.Assert.assertArrayEquals;
   import static org.junit.Assert.assertEquals;

   import com.google.gson.Gson;

   import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
   import com.teamtreehouse.techdegrees.model.Todo;
   import com.teamtreehouse.techdegrees.Testing.ApiClient;
   import com.teamtreehouse.techdegrees.Testing.ApiResponse;
   import org.junit.After;
   import org.junit.AfterClass;
   import org.junit.Before;
   import org.junit.BeforeClass;
   import org.junit.Test;
   import org.sql2o.Connection;
   import org.sql2o.Sql2o;
   import java.util.HashMap;
   import java.util.Map;
   import spark.Spark;

public class AppTest {
    public static final String TEST_PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:test";
    private Sql2oTodoDao todoDao;
    private Sql2o sql2o;
    private Connection conn;
    private ApiClient client;
    private Gson gson;

    @BeforeClass
    public static void StartServer() throws Exception {
        String[] args = {TEST_PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void StopServer() throws Exception {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        todoDao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + TEST_PORT);
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void requestingTodosReturnsAll() throws Exception {
        todoDao.add(new Todo("First",false));
        todoDao.add(new Todo("Second",false));

        ApiResponse res = client.request("GET", "/api/v1/todos");
        Todo[] todos = gson.fromJson(res.getBody(), Todo[].class);

        assertEquals(2, todos.length);
    }

    @Test
    public void postingTodoReturns201Status() throws Exception {
        Todo todo = new Todo("Test", false);

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(todo));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void postingTodoSavesEntry() throws Exception {
        Todo todo = new Todo("test", false);

        client.request("POST", "/api/v1/todos", gson.toJson(todo));

        assertEquals(1, todoDao.findAll().size());
        assertEquals("test", todoDao.findById(1).getTask());
        assertEquals(false, todoDao.findById(1).isCompleted());
    }

    @Test
    public void puttingTodoChangesNameAndCompleted() throws Exception {
        Todo todo = new Todo("test", false);
        todoDao.add(todo);
        Map<String, Object> newTodo = new HashMap<>();
        newTodo.put("task", "updated name");
        newTodo.put("completed", true);

        client.request("PUT", String.format("/api/v1/todos/%d",todo.getId()), gson.toJson(newTodo));

        assertArrayEquals(new Object[]{"updated name", true},
                new Object[]{todoDao.findById(todo.getId()).getTask(), todoDao.findById(todo.getId()).isCompleted()});
    }

    @Test
    public void deleteTodoDeletesProperTodo() throws Exception {
        Todo todo = new Todo("test", false);
        todoDao.add(todo);

        client.request("DELETE", String.format("/api/v1/todos/%d", todo.getId()));

        assertEquals(todoDao.findAll().size(), 0);
    }

    @Test
    public void deletingReturns204Status() throws Exception {
        Todo todo = new Todo("test", false);
        todoDao.add(todo);

        ApiResponse res = client.request("DELETE", String.format("/api/v1/todos/%d", todo.getId()));

        assertEquals(204, res.getStatus());
    }

    @Test
    public void deletingReturnsEmptyBody() throws Exception {
        Todo todo = new Todo("test", false);
        todoDao.add(todo);

        ApiResponse res = client.request("DELETE", String.format("/api/v1/todos/%d", todo.getId()));

        assertEquals("", res.getBody());
    }
}