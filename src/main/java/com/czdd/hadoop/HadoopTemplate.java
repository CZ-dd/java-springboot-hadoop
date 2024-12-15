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

import java.io.IOException;

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
}
