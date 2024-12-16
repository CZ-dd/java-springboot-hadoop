package com.czdd.hadoop;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@ConditionalOnBean(FileSystem.class)
@Slf4j
public class HadoopTemplate {

    @Autowired
    private FileSystem fileSystem;

    @Value("${hadoop.name-node}")
    private String nameNode;

    @Value("${hadoop.namespace}")
    private String nameSpace;

    @PostConstruct
    public void init(){
        existDir(nameSpace, true);
    }

    public void uploadFile(String srcFile){
        copyFileToHDFS(false, true, srcFile, nameSpace);
    }

    public void uploadFile(boolean del, String srcFile){
        copyFileToHDFS(del, true, srcFile, nameSpace);
    }

    public void uploadFile(String srcFiles, String destPath){
        copyFileToHDFS(false, true, srcFiles, destPath);
    }

    public void uploadFile(boolean del, String srcFiles, String destPath){
        copyFileToHDFS(del, true, srcFiles, destPath);
    }

    public void delFile(String fileName){
        rmdir(nameSpace, fileName);
    }

    public void delDir(String path){
        path = nameSpace + "/" + path;
        rmdir(path, null);
    }

    public void download(String fileName, String savePath){
        getFile(nameSpace + "/" + fileName, savePath);
    }

    /*
     * @description:文件上传至 HDFS
     * @param delSrc        是否删除源文件
     * @param overwrite
     * @param srcFile       源文件，上传文件路径
     * @param destPath      hdfs目的路径
            * @return: void
            * @author: Cz_13
            * @time: 2024/12/14 16:12
     */
    private void copyFileToHDFS(boolean delSrc, boolean overwrite, String srcFile, String destPath) {
        // 源文件
        Path srcPath = new Path(srcFile);

        // 目的路径
        if(StringUtils.isNotBlank(nameNode)){
            destPath = nameNode + destPath;
        }
        Path dstPath = new Path(destPath);

        // 文件上传
        try {
            fileSystem.copyFromLocalFile(srcPath, dstPath);
            fileSystem.copyFromLocalFile(delSrc, overwrite, srcPath, dstPath);
        } catch (IOException e){
            log.error("", e);
        }
    }

    /*
     * @description: 删除文件/目录
     * @param path
     * @param fileName
            * @return: void
            * @author: Cz_13
            * @time: 2024/12/14 16:20
     */
    public void rmdir(String path, String fileName){
        try {
            if(StringUtils.isNotBlank(nameNode)){
                path = nameNode + path;
            }
            if(StringUtils.isNotBlank(fileName)){
                path = path + "/" +fileName;
            }
            fileSystem.delete(new Path(path), true);
        } catch (IllegalArgumentException | IOException e) {
            log.error("", e);
        }
    }

    /*
     * @description: 创建目录
     * @param filePath
     * @param create
            * @return: boolean
            * @author: Cz_13
            * @time: 2024/12/14 16:27
     */
    private boolean existDir(String filePath, boolean create) {
        boolean flag = false;
        if(StringUtils.isEmpty(filePath)){
            throw new IllegalArgumentException("filePath不能为空");
        }
        try {
            Path path = new Path(filePath);
            if(create){
                if(!fileSystem.exists(path)){
                    fileSystem.mkdirs(path);
                }
            }
            if (fileSystem.isDirectory(path)){
                flag = true;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return flag;
    }

    /*
     * @description: 从HDFS下载文件
     * @param hdfsFile
     * @param destPath
            * @return: void
            * @author: Cz_13
            * @time: 2024/12/14 16:32
     */
    public void getFile(String hdfsFile, String destPath){
        // 源文件路径
        if(StringUtils.isNotBlank(nameNode)){
            hdfsFile = nameNode + hdfsFile;
        }

        Path hdfsPath = new Path(hdfsFile);
        Path dstPath = new Path(destPath);
        try {
            fileSystem.copyToLocalFile(hdfsPath, dstPath);
        } catch (IOException e) {
            log.error("", e);
        }
    }
    /*
     * @description:执行mapreduce任务
     * @param inPath
     * @param outPath
            * @return: java.lang.String
            * @author: Cz_13
            * @time: 2024/12/16 12:58
     */
    public String mapreduce(String inPath, String outPath){
        ProcessBuilder processBuilder = new ProcessBuilder("hadoop", "jar", "/export/jar/mr03.jar", "hadoop.mapreduce.WordCountDriver", inPath, outPath);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return "MapReduce job completed successfully: " + stringBuilder.toString();
            } else {
                return "Error1: " + stringBuilder.toString();
            }

        } catch (IOException | InterruptedException e) {
            log.error("", e);
            return "Error2: " + e.getMessage();
        }

        /*Configuration configuration = new Configuration();
        // configuration.set("fs.defaultFS", nameNode);
        configuration.set("fs.defaultFS", "hdfs://node1:8020");
        Job job = Job.getInstance(configuration, "MyWordCount");
        *//*String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();   // 其他参数inPath 和 outPath
        if(otherArgs.length < 2){
            System.err.println("usage: hadoop jar WordCount.jar hadoop.mapreduce.WordCountDriver <in> <out>");
            System.exit(2);
        } else {
            for (int i = 0; i < otherArgs.length - 1; i++) {
                if(!("hadoop.mapreduce.WordCountDriver".equalsIgnoreCase(otherArgs[i]))){
                    FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
                    // System.out.println("参数in: " + otherArgs[i]);
                }
            }
            FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        }*//*
        String inPath = "hdfs://node1:8020/input/test1.txt";
        String outPath = "hdfs://node1:8020/output/test2/";
        // FileSystem fs = FileSystem.get(URI.create("hdfs://node1:8020"), configuration, "hadoop");
        if(fileSystem.exists(new Path(outPath))){
            // 存在的话删除
            fileSystem.delete(new Path(outPath), true);
        }

        job.setJarByClass(HadoopTemplate.class);
        // job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inPath));
        FileOutputFormat.setOutputPath(job, new Path(outPath));
        System.exit((job.waitForCompletion(true) ? 0 : 1));*/
    }
}
