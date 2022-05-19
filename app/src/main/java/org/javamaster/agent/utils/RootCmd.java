package org.javamaster.agent.utils;

import org.javamaster.agent.common.App;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author yudong
 * @date 2020/6/17
 */
public class RootCmd {

    public static void modifyHosts(String pcHostsContent) throws Exception {
        String hostsPath = hosts(pcHostsContent);
        delFile("/system/etc/hosts");
        cpFile(hostsPath, "/system/etc");
    }

    @SuppressWarnings("ALL")
    public static String hosts(String pcHostsContent) throws Exception {
        exusecmd("mount -o rw,remount /system");
        exusecmd("chmod 777 /system");
        File file = new File(App.context.getCacheDir(), "hosts");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        exusecmd("chmod 777 " + file.getAbsolutePath());
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(pcHostsContent);
        fileWriter.close();
        return file.getAbsolutePath();
    }

    public static void cpFile(String filePath, String sysFilePath) throws Exception {
        exusecmd("mount -o rw,remount /system");
        exusecmd("chmod 777 /system");
        exusecmd("cp -a " + filePath + " " + sysFilePath);
        exusecmd("chmod 644 " + sysFilePath + "/hosts");
        exusecmd("chown root:root " + sysFilePath + "/hosts");
        exusecmd("chgrp root " + sysFilePath + "/hosts");
    }

    public static void delFile(String filePath) throws Exception {
        exusecmd("mount -o rw,remount /system");
        exusecmd("chmod 777 /system");
        exusecmd("rm -rf " + filePath);
    }

    public static String exusecmd(String command) throws Exception {
        Process process = null;
        DataOutputStream os = null;
        InputStream is = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            is = process.getInputStream();
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
        }catch (Exception e){
            return "error:" + e.getMessage();
        }finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
    }

}
