package edu.ptit.da2020;

import edu.ptit.da2020.init.DataInit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestSomething {
    @Autowired
    DataInit dataInit;

    @Test
    public void contextLoads(){

    }

    @Test
    public void aaa() {
        Set<String> set = new HashSet<>();
        Map<Integer, String> map = dataInit.getListName();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            set.add(entry.getValue().split("::")[1]);
        }
        System.out.println(set.size());
    }
}
