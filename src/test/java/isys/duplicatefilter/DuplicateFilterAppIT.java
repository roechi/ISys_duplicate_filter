package isys.duplicatefilter;

import isys.duplicatefilter.config.WebConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DuplicateFilterApp.class, WebConfiguration.class},
    properties = "scheduling.enabled=false"
)
public class DuplicateFilterAppIT {

    @Test
    public void contextLoads() {
        //ok
    }

}