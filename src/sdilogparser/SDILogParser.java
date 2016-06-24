/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdilogparser;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author ninjo
 */
public class SDILogParser {
//Vars
    public static String user = "jhutchins";
    public static String password = "F1shing@Sea";
    public static String host = "192.168.1.3";
    public static int port = 22;
    public static JSch jsch = new JSch();
    public static ChannelSftp sftpChannel;
    public static String remoteFilePath = "/export/logs/sonicwall/archive/local0-2016-06-08-09";
    public static Session session;

    public static void main(String[] args) {
        //Setup-------------------------------------------------------------------
        connectSSH();
        File urlFile = new File("C:\\Users\\ninjo\\Desktop\\urlData.csv");
        try {
            urlFile.createNewFile();
            System.out.println("URL file created!");
        } catch (Exception e) {
            System.out.println("Error creating URL file!");
        }

        try {
            InputStream out = null;
            out = sftpChannel.get(remoteFilePath);

            Scanner scan = new Scanner(out);
            PrintWriter writer = new PrintWriter(urlFile);

            String newDat = "";
            String needed = "dstname=";

            while (scan.hasNext()) {
                newDat = scan.next();
                if (newDat.contains(needed)) {
                    newDat = newDat.substring(8);
                    writer.println(newDat);
                }
            }
        } catch (Exception e) {

        }
    }

    public static void connectSSH() {
        try {
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
        } catch (Exception e) {
            System.err.print(e);
        }
    }
}