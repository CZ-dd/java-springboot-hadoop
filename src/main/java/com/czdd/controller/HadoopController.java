package com.czdd.controller;

import com.czdd.hadoop.HadoopTemplate;
import com.czdd.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hdfs")
public class HadoopController {

    /*@Autowired
    private HadoopService hadoopService;

    @PostMapping("/createFile")
    public Result createFile(@RequestBody SimpleFile sf){
        try {
            hadoopService.createFile(sf.getFilePath(), sf.getContent());
            return Result.success();
        } catch (IOException e) {
            // e.printStackTrace();
            return Result.error("失败");
        }
    }*/

    @Autowired
    private HadoopTemplate hadoopTemplate;

    @PostMapping("/upload")
    public Result upload(@RequestParam String srcFile){
        hadoopTemplate.uploadFile(srcFile);
        return Result.success("upload");
    }

    @DeleteMapping("/delFile")
    public Result del(@RequestParam String fileName){
        hadoopTemplate.delFile(fileName);
        return Result.success("delete file");
    }

    @GetMapping("/download")
    public Result download(@RequestParam String fileName, @RequestParam String savePath){
        hadoopTemplate.download(fileName, savePath);
        return Result.success("download");
    }

    @GetMapping("/mapreduce")
    public Result mr(@RequestParam String inPath, @RequestParam String outPath){
        return Result.success(hadoopTemplate.mapreduce(inPath, outPath));
    }

}
