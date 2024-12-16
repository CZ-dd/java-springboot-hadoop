package com.czdd.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConditionalOnProperty(name="hadoop.name-node")
@Slf4j
public class HadoopConfig {
    @Value("${hadoop.fs.defaultFS}")
    private String hadoopUri;

    @Value("${hadoop.name-node}")
    private String nameNode;

    @Bean("fileSystem")
    public FileSystem createFs() throws Exception{
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", nameNode);
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(URI.create(hadoopUri), configuration, "hadoop");
        } catch (Exception e) {
            log.error("", e);
        }
        System.out.println("fs.defaultFS: " + configuration.get("fs.defaultFS"));
        return fileSystem;
    }
}
