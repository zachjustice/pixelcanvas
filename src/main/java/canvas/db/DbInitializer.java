package canvas.db;

import canvas.pixel.PixelChangesListener;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DbInitializer implements InitializingBean {
    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

    @Autowired
    private PixelChangesListener chatChangesListener;

    private static final RethinkDB r = RethinkDB.r;

    @Override
    public void afterPropertiesSet() throws Exception {
        chatChangesListener.pushChangesToWebSocket();
    }

    public void createDb() {
        Connection connection = connectionFactory.createConnection();
        List<String> dbList = r.dbList().run(connection);
        if (!dbList.contains("canvas")) {
            r.dbCreate("canvas").run(connection);
        }
        List<String> tables = r.db("canvas").tableList().run(connection);
        if (!tables.contains("pixels")) {
            r.db("canvas").tableCreate("pixels").run(connection);
            r.db("canvas").table("pixels").run(connection);
        }
    }
}