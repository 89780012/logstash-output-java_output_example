# Logstash Java Plugin

[![Travis Build Status](https://travis-ci.org/logstash-plugins/logstash-output-java_output_example.svg)](https://travis-ci.org/logstash-plugins/logstash-output-java_output_example)

This is a Java plugin for [Logstash](https://github.com/elastic/logstash).

It is fully free and fully open source. The license is Apache 2.0, meaning you are free to use it however you want.

The documentation for Logstash Java plugins is available [here](https://www.elastic.co/guide/en/logstash/6.7/contributing-java-plugin.html).

本插件只做了数据转发。

# 编译
  在项目根路径 window gradlew.bat gem

# 安装
  bin/logstash-plugin install --no-verify --local logstash-output-java_output_example.gem
  
# 使用
  output {
    java_output_example {
      hostname=>"192.168.192.1"
      port=>26666
      retry=>-1
      retrytime=>3000
    }
  }
  在你的项目中 请使用 socket 监听数据。 本项目传输实体类基于公司项目，大家可自行改造
  
  感谢开源世界！
  


