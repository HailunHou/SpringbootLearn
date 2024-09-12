package com.hhl.springbootlearn.controller;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author hhl
 * @version 1.0
 * @description
 * @date 2024/9/11 22:20
 */
@RestController
@RequestMapping("ftp")
public class DownloadFtpFileController {

    @GetMapping("/getfile")
    public String getFile(@RequestParam("path") String path){
        FTPClient ftpClient = new FTPClient();
        String server = "192.168.1.4";  // FTP 服务器地址
        int port = 21;  // FTP 默认端口
        String user = "root";  // FTP 用户名
        String pass = "root";  // FTP 密码

        try {
            // 连接到 FTP 服务器
            ftpClient.connect(server, port);
            // 登录 FTP 服务器
            boolean login = ftpClient.login(user, pass);

            if (login) {
                System.out.println("FTP 登录成功");

                // 设置文件类型为二进制文件类型
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // 指定下载的文件路径和本地保存路径
                String remoteFilePath = "/1.jpg";
                String localFilePath = "E:/1.jpg";

                // 创建本地文件输出流
                try (FileOutputStream fos = new FileOutputStream(localFilePath)) {
                    // 从 FTP 服务器下载文件
                    boolean success = ftpClient.retrieveFile(remoteFilePath, fos);
                    if (success) {
                        System.out.println("文件下载成功");
                    } else {
                        System.out.println("文件下载失败");
                    }
                }

            } else {
                System.out.println("FTP 登录失败");
            }

            // 退出并断开连接
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ok";
    }


}
