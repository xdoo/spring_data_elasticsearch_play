package com.example.elasticsearch;

import com.example.elasticsearch.model.BarTask;
import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.model.FooTask;
import com.example.elasticsearch.model.Task;
import com.example.elasticsearch.services.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ElasticsearchApplicationTests {

	@Autowired
	CaseService service;

	@Test
	public void testSave() {
        Case one = this.service.saveOne("BBB");
        log.info("ID --> {}", one.getCaseID());
        one.setText("Hansi Booo");
        this.service.updateIt(one);
    }

    @Test
    public void testSaveId() {
        String id = RandomStringUtils.randomAlphanumeric(20).toUpperCase();
        log.info("ID --> {}", id);
        Case aCase = this.service.saveSomething(id, "RAUL");
        log.info("ID --> {}", aCase.getCaseID());

        // add geopoint
        GeoPoint geoPoint = new GeoPoint(48.173243, 11.536002);
        Case aCase1 = this.service.addGeoPoint(geoPoint, aCase);

        // add some nested items
        FooTask fooTask = new FooTask();
        fooTask.setName("My fancy Foo Task!");
        fooTask.setDate(LocalDate.now());
        fooTask.setFoo("yo - foo");

        BarTask barTask = new BarTask();
        barTask.setName("Ok - doing some bar");
        barTask.setDate(LocalDate.now());
        barTask.setBar("baaaaar");

        ArrayList<Task> tasks = Lists.newArrayList(fooTask, barTask);
        aCase1.setTasks(tasks);
        this.service.updateIt(aCase1);

    }

}

