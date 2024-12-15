package com.czdd.service.impl;

import com.czdd.service.HadoopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
@org.springframework.context.annotation.Configuration
@Slf4j
public class HadoopServiceImpl implements HadoopService {
    @Value("${hadoop.fs.defaultFS}")
    private String hadoopUri;

    @Value("${hadoop.name-node}")
    private String nameNode;


/*    public FileSystem createFs() throws Exception{
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", nameNode);
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(URI.create(hadoopUri), configuration, "hadoop");
        } catch (Exception e) {
            log.error("", e);
        }
        System.out.println("fs.defaultFS: " + configuration.get("fs.defaultFS"));
        return fileSystem;

    }*/

    @Override
    public void createFile(String filePath, String content) throws IOException {
        // 配置Hadoop 文件系统
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", nameNode);
        FileSystem fileSystem = FileSystem.get(URI.create(hadoopUri), configuration);

        // 创建文件
        Path path = new Path(filePath);
        try (FSDataOutputStream outputStream = fileSystem.create(path)){
            outputStream.writeUTF(content);
        }
    }
}
