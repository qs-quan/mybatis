import com.wayzim.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
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

        //关闭资源
        sqlSession.close();
    }

    @Test
    public void findUsersByNameTest() throws Exception {
        //读取配置文件
        //全局配置文件的路径
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用SqlSession的增删改查方法
        //第一个参数：表示statement的唯一标示
        List<User> list = sqlSession.selectOne("test.findUsersByName", "小明");
        System.out.println(list);
        //关闭资源
        sqlSession.close();
    }

    @Test
    public void insertUserTest() throws Exception {
        //读取配置文件
        //全局配置文件的路径
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //创建SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        User user = new User();

        //调用SqlSession的增删改查方法
        //第一个参数：表示statement的唯一标示
        sqlSession.insert("test.insertUser", user);

        System.out.println(user.getId());
        //提交事务
        sqlSession.commit();
        //关闭资源
        sqlSession.close();
    }


}


