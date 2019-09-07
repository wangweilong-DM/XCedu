package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/9/5 13:59
 */
@Service
public class FileSystemService {



    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    @Autowired
    FileSystemRepository fileSystemRepository;

    /**
     * 上传文件
     *
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        //第一步，将文件上传到fastDFS中，得到一个文件id
        String fileId = uploadFileToDfs(multipartFile);

        if (StringUtils.isEmpty(fileId)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        //第二部，将文件id以及其他文件信息存储到mongoDB中。
        FileSystem fileSystem = new FileSystem();
        //存数据
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());

        if (!StringUtils.isEmpty(metadata)){
            Map metadatamap = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(metadatamap);
        }


        FileSystem save = fileSystemRepository.save(fileSystem);
        UploadFileResult uploadFileResult = new UploadFileResult(CommonCode.SUCCESS,save);

        return uploadFileResult;
    }

    //上传文件到fastDFS中
    private String uploadFileToDfs(MultipartFile multipartFile) {

        initFdfs();
        //定义TrackerClient，用于请求TrackerServer
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        try {
            trackerServer = trackerClient.getConnection();
            storageServer = trackerClient.getStoreStorage(trackerServer);

            //获取Stroage
            //创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            //向stroage服务器上传文件

            //得到文件字节
            byte[] bytes = multipartFile.getBytes();
            //得到文件的原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            //得到扩展名
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length());

            //上传成功后得到文件Id
            String fileId = storageClient1.upload_file1(bytes, substring, null);


            return fileId;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //初始化fdfs服务
    private void initFdfs() {
        //初始化tracker服务地址，
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
           ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出异常
            ExceptionCast.cast(FileSystemCode.FS_INIT_ERROR);
        }

    }
}
