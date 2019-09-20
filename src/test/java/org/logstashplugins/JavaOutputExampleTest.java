package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Event;
import org.junit.Assert;
import org.junit.Test;
import org.logstash.plugins.ConfigurationImpl;
import org.logstashplugins.JavaOutputExample;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JavaOutputExampleTest {

    @Test
    public void testJavaOutputExample() {
        Map<String, Object> configValues = new HashMap<>();
        configValues.put(JavaOutputExample.HOSTNAME.name(), "localhost");
        configValues.put(JavaOutputExample.PORT.name(), Long.valueOf(26666));
        configValues.put(JavaOutputExample.RETRY.name(), Long.valueOf(-1));
        configValues.put(JavaOutputExample.RETRYTIME.name(), Long.valueOf(2000));


        Configuration config = new ConfigurationImpl(configValues);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JavaOutputExample output = new JavaOutputExample("test-id", config, null, baos);

        String sourceField = "message";
        int eventCount = 5;
        Collection<Event> events = new ArrayList<>();
        for (int k = 0; k < eventCount; k++) {
            Event e = new org.logstash.Event();
            e.setField("type", "type 中文测试" + k);
            e.setField("strategy_name", "strategy_name " + k);
            e.setField("create_time", "create_time " + k);
            e.setField("state", "state " + k);
            e.setField("content", "content " + k);
            e.setField("logpath", "logpath " + k);
            events.add(e);
        }

        output.output(events);

        String outputString = baos.toString();
        int index = 0;
        int lastIndex = 0;
        while (index < eventCount) {
            //lastIndex = outputString.indexOf(prefix, lastIndex);
            //Assert.assertTrue("Prefix should exist in output string", lastIndex > -1);
            //lastIndex = outputString.indexOf("message " + index);
            //Assert.assertTrue("Message should exist in output string", lastIndex > -1);
            //index++;
        }
    }
}
