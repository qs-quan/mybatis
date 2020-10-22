import com.wayzim.mapper.UserMapper;
import com.wayzim.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import resulthandler.MyResultHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-09-05 14:18
 */
public class MybaitisTest {

    @Test
    public void findAllTest() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用SqlSession的增删改查方法
        //第一个参数：表示statement的唯一标示
        List<User> users = sqlSession.selectList("com.wayzim.mapper.UserMapper.selectAll");
        System.out.println(users);

        MyResultHandler myResultHandler = new MyResultHandler();
        sqlSession.select("com.wayzim.mapper.UserMapper.selectAll", myResultHandler);
        //关闭资源
        sqlSession.close();
    }


    @Test
    public void findById() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        System.out.println("===========typeHandler======");
        User user = mapper.selectById("253772566311735297");
        System.out.println(user);
        //关闭资源
        sqlSession.close();
    }


    /**
     * 同个session进行两次相同查询
     * 结论：MyBatis只进行1次数据库查询。
     *
     * @throws Exception
     */
    @Test
    public void firstLevelCacheTest1() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 一级缓存默认开启 作用域 sqlSession
        User user = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");
        User user1 = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");
        //关闭资源
        sqlSession.close();
    }


    /**
     * 同个session进行两次不同的查询。
     * 结论：MyBatis进行两次数据库查询。
     *
     * @throws Exception
     */
    @Test
    public void firstLevelCacheTest2() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 一级缓存默认开启 作用域 sqlSession
        User user = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");
        User user1 = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "2");
        //关闭资源
        sqlSession.close();
    }

    /**
     * 不同session，进行相同查询。
     * 结论：MyBatis进行两次数据库查询。
     *
     * @throws Exception
     */
    @Test
    public void firstLevelCacheTest3() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 一级缓存默认开启 作用域 sqlSession
        User user = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");
        //关闭资源
        sqlSession.close();

        //创建SqlSession
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        // 一级缓存默认开启 作用域 sqlSession
        sqlSession1.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");
        //关闭资源
        sqlSession1.close();
    }


    /**
     * 同个session，查询之后更新数据，再次查询相同的语句。
     * 更新操作之后缓存会被清除：
     *
     * @throws Exception
     */
    @Test
    public void firstLevelCacheTest4() throws Exception {
        //读取配置文件   全局配置文件的路径
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        // 一级缓存默认开启 作用域 sqlSession
        User user = sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");

        HashMap<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(user.getId()));
        param.put("username", user.getUsername() + "111");
        sqlSession.update("com.wayzim.mapper.UserMapper.updateById", param);

        sqlSession.selectOne("com.wayzim.mapper.UserMapper.selectById", "253772566311735297");

        //关闭资源
        sqlSession.close();

    }


}


