package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.Output;
import co.elastic.logstash.api.PluginConfigSpec;
import com.alibaba.fastjson.JSON;
import com.zts.client.TcpClient;
import com.zts.model.LogMessage;
import io.netty.channel.Channel;
import org.jruby.RubyString;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.CountDownLatch;

// class name must match plugin name
@LogstashPlugin(name = "java_output_example")
public class JavaOutputExample implements Output {

    public static final PluginConfigSpec<String> HOSTNAME =
            PluginConfigSpec.stringSetting("hostname", "localhost");

    public static final PluginConfigSpec<Long> PORT =
            PluginConfigSpec.numSetting("port",26666);

    // 重连次数， -1 表示不限制
    public static final PluginConfigSpec<Long> RETRY=
            PluginConfigSpec.numSetting("retry", -1);

    // 每次重连的时间，默认为 5000 ms
    public static final PluginConfigSpec<Long> RETRYTIME=
            PluginConfigSpec.numSetting("retrytime", 5000);


    private final String id;
    private final CountDownLatch done = new CountDownLatch(1);
    private volatile boolean stopped = false;

    // 定义转发参数
    private String hostname;
    private int port;
    private int retry;
    private Long retrytime;
    private TcpClient tcpClient;


    // all plugins must provide a constructor that accepts id, Configuration, and Context
    public JavaOutputExample(final String id, final Configuration configuration, final Context context) {
        this(id, configuration, context, null);
    }

    JavaOutputExample(final String id, final Configuration config, final Context context, OutputStream targetStream) {
        // constructors should validate configuration options
        this.id = id;
        // 初始化相应参数
        hostname = config.get(HOSTNAME);
        port = config.get(PORT).intValue();
        retry = config.get(RETRY).intValue();
        retrytime = config.get(RETRYTIME);


        tcpClient = new TcpClient(hostname, port,retry,retrytime);
        tcpClient.connect();
    }

    @Override
    public void output(final Collection<Event> events) {
        Iterator<Event> z = events.iterator();
        while (z.hasNext() && !stopped) {
            //String s = prefix + z.next();
            //printer.println(s);
            Map<String, Object> map = z.next().getData();
            LogMessage message = new LogMessage();
            message.setType( ((RubyString) map.get("type")).asJavaString());
            message.setType( ((RubyString) map.get("type")).asJavaString());
            message.setStrategy_name(((RubyString) map.get("strategy_name")).asJavaString());
            message.setCreate_time(((RubyString) map.get("create_time")).asJavaString());
            message.setState(((RubyString) map.get("state")).asJavaString());
            message.setContent(((RubyString) map.get("content")).asJavaString());
            message.setLogpath(((RubyString) map.get("logpath")).asJavaString());
            String json = JSON.toJSONString(message);
            //System.out.println("发送数据为:" + json);
            Channel channel = tcpClient.getChannel();
            if(channel!=null){
                channel.writeAndFlush(json);
            }
//            socketClient.transport(message);
//            Set<String> keySet = map.keySet();
//            printer.println("===============================");
//            for(String key: keySet){
//                printer.println("key:" + key + "===value:" + map.get(key));
//
//            }
//            printer.println("===============================");
        }
    }

    @Override
    public void stop() {
        stopped = true;
        done.countDown();
    }

    @Override
    public void awaitStop() throws InterruptedException {
        done.await();
    }


    //配置文件列表
    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        List<PluginConfigSpec<?>> list = new ArrayList<>();
        list.add(HOSTNAME);
        list.add(PORT);
        list.add(RETRY);
        list.add(RETRYTIME);
        return list;
    }

    @Override
    public String getId() {
        return id;
    }
}
