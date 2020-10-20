package resulthandler;

import com.wayzim.pojo.User;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

/**
 * ${DESCRIPTION}
 *
 * @author 14684
 * @create 2020-10-16 10:57
 */
public class MyResultHandler implements ResultHandler<User> {



    @Override
    public void handleResult(ResultContext<? extends User> resultContext) {
        User user = resultContext.getResultObject();
        user.setUsername("hello===" + user.getUsername());
    }

}
