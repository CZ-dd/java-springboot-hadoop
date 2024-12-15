package com.czdd.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface HadoopService {
    public void createFile(String filePath, String content) throws IOException;
}
