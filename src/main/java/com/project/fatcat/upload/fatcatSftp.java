package com.project.fatcat.upload;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP Util
 * 
 */
public class fatcatSftp {
	
	//private Log log = LogFactory.getLog(SFTPUtil.class);
	
	public Session session = null;
	public Channel channel = null;
	public ChannelSftp channelSftp = null;
	
	final private String uploadDir = "/fatcat";

	/**
	 * SFTP 접속
	 * 
	 * @param ip
	 * @param port
	 * @param id
	 * @param pw
	 * @param privateKey
	 */
	public void sftpInit(String ip, int port, String id, String pw, String privateKey) throws Exception {
		String connIp = ip;		//접속 SFTP 서버 IP
		int connPort = port;	//접속 PORT
		String connId = id;		//접속 ID
		String connPw = pw;		//접속 PW
		int timeout = 10000; 	//타임아웃 10초
		
		JSch jsch = new JSch();
		try {
			InetAddress local;
            local = InetAddress.getLocalHost();

            //key 인증방식일경우
            if(null != privateKey && !"".equals(privateKey)) {
            	jsch.addIdentity(privateKey);
            }
            
            //세션객체 생성 
            session = jsch.getSession(connId, connIp, connPort);
            
            if(null == privateKey || "".equals(privateKey)) {
            	session.setPassword(connPw); //password 설정
            }
            
            //세션관련 설정정보 설정
            java.util.Properties config = new java.util.Properties();
            
            //호스트 정보 검사하지 않는다.
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(timeout); //타임아웃 설정
            
            //log.info("connect.. " + connIp);
            System.out.println("connect.. " + connIp);
            session.connect();	//접속
            
            channel = session.openChannel("sftp");	//sftp 채널 접속
            channel.connect();
            
		} catch (JSchException e) {
            //log.error(e);
			System.out.println(e);
            throw e;
        } catch (Exception e) {
            //log.error(e);
        	System.out.println(e);
            throw e;
		}
		channelSftp = (ChannelSftp) channel;
	}
	
	/**
	 * SFTP 서버 접속 종료
	 */
	public void disconnect() {
		if(channelSftp != null) {
			channelSftp.quit();
		}
		if(channel != null) {
			channel.disconnect();
		}
		if(session != null) {
			session.disconnect();
		}
	}
	
	/**
	 * SFTP 서버 파일 업로드
	 * @param uploadPath
	 * @param localPath
	 * @param uploadFileNm
	 */
	public void sftpFileUpload(String uploadPath, String localPath, String uploadFileNm) throws Exception {
		
		
		FileInputStream in = null;
		
		try{ 
			sftpInit("ivisus.iptime.org", 9022, "kwlee", "Rmsdn760223!", null);
			//파일을 가져와서 inputStream에 넣고 저장경로를 찾아 업로드 
			in = new FileInputStream(localPath+uploadFileNm);
			channelSftp.cd(uploadPath);
			channelSftp.put(in,uploadFileNm);
			//log.info("sftpFileUpload success.. ");
			System.out.println("sftpFileUpload success.. ");
		}catch(SftpException se){
			//log.error(se);
			System.out.println(se);
			throw se;
		}catch(FileNotFoundException fe){
			//log.error(fe);
			System.out.println(fe);
			throw fe;
		} catch (Exception e) {
			//log.error(e);
			System.out.println(e);
			throw e;
		}finally{
			try{
				in.close();
			} catch(IOException ioe){
				//log.error(ioe);
				System.out.println(ioe);
			}
			try{
				disconnect();
			} catch(Exception e){
				//log.error(ioe);
				System.out.println(e);
			}
		}
	}
	
	/**
	 * SFTP 서버 파일 업로드
	 * @param uploadPath
	 * @param localPath
	 * @param uploadFileNm
	 */
	public void sftpFileUpload(MultipartFile mpf) throws Exception {
		
		
		FileInputStream in = null;
		InputStream is = mpf.getInputStream();
		
		try{ 
			sftpInit("ivisus.iptime.org", 2202, "edu", "Edu13@$", null); 
			channelSftp.cd(uploadDir);
			channelSftp.put(is, mpf.getOriginalFilename());
			System.out.println("sftpFileUpload success.. ");
		}catch(SftpException se){
			//log.error(se);
			System.out.println(se);
			throw se;
		}catch(FileNotFoundException fe){
			//log.error(fe);
			System.out.println(fe);
			throw fe;
		} catch (Exception e) {
			//log.error(e);
			System.out.println(e);
			throw e;
		}finally{
			try{
				in.close();
			} catch(IOException ioe){
				//log.error(ioe);
				System.out.println(ioe);
			}
			try{
				disconnect();
			} catch(Exception e){
				//log.error(ioe);
				System.out.println(e);
			}
		}
	}
	
	/**
	 * SFTP 서버 파일 업로드 (MultipartFile + 원하는 파일명)
	 */
	public void sftpFileUpload(MultipartFile mpf, String uploadDir, String uploadFileNm) throws Exception {
	    InputStream is = null;
	    try {
	        sftpInit("ivisus.iptime.org", 2202, "edu", "Edu13@$", null);

	        // 디렉토리 이동
	        channelSftp.cd(uploadDir);

	        // 업로드 (UUID 붙인 파일명으로)
	        is = mpf.getInputStream();
	        channelSftp.put(is, uploadFileNm);

	        System.out.println("sftpFileUpload success.. ");
	    } catch (Exception e) {
	        System.out.println("Upload failed: " + e.getMessage());
	        throw e;
	    } finally {
	        if (is != null) try { is.close(); } catch (IOException ignored) {}
	        try { disconnect(); } catch (Exception ignored) {}
	    }
	}


}