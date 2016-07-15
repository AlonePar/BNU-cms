package cn.edu.bnu.test.config;

import cn.edu.bnu.cms.Application;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dave on 16/7/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataSourceTest {
    @Autowired
    private DataSource ds;

    @Test
    public void dataSourceExists() {
        assertThat(ds).isNotNull();
        assertThat(ds).isInstanceOf(HikariDataSource.class);
    }
}
