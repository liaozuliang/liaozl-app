package com.liaozl.utils.email;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


/**
 * 基于Spring框架的邮件发送服务
 */
public class EmailSender {
	
	private Logger log = Logger.getLogger(EmailSender.class);
	
	private String CONFIG_FILE_PATH = "/email.properties";
	
	private JavaMailSenderImpl mailSender;
	private String fromAccount;
	
	public EmailSender(){
		init();
	}
	
	
	/**
	 * 初始化邮件服务配置
	 */
	private void init() {
		try {
			mailSender = new JavaMailSenderImpl();

			Properties props = new Properties();
			props.put("mail.transport.protocol", "smtp");

			Properties pop = new Properties();
			pop.load(EmailSender.class.getResourceAsStream(CONFIG_FILE_PATH));

			// 邮件服务器地址
			String host = pop.getProperty("mail.smtp.host");
			mailSender.setHost(host.trim());

			// 邮件服务器端口，一般是25
			int port = NumberUtils.toInt(pop.get("mail.smtp.port").toString().trim(), 25);
			mailSender.setPort(port);

			// 是否需要认证
			props.put("mail.smtp.auth", "true");

			// 发件邮件的账号
			fromAccount = pop.getProperty("mail.from.account");
			mailSender.setUsername(fromAccount.trim());

			// 发件邮件的密码
			String password = pop.getProperty("mail.from.password");
			mailSender.setPassword(password.trim());

			// 是否是调试模式
			props.put("mail.debug", "false");
			mailSender.setDefaultEncoding("UTF-8");

			String isDebug = pop.getProperty("mail.smtp.debug");
			if (StringUtils.isNotEmpty(isDebug) && "true".equals(isDebug.trim())) {
				props.put("mail.debug", "true");
			}

			// 是否使用SSL (126等一般邮件服务不需要开启，gmail等则需要开启)
			String useSSL = pop.getProperty("mail.smtp.useSSL");
			if (StringUtils.isNotEmpty(useSSL) && "true".equals(useSSL.trim())) {
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.socketFactory.fallback", "false");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.socketFactory.port", port + "");
			}

			mailSender.setJavaMailProperties(props);
		} catch (IOException e) {
			log.error("加载邮件配置文件错误：", e);
		}
	}
	
	
	/**
	 * 发送纯文本内容邮件
	 * @param toAccount 收件人
	 * @param ccAccount 抄送人
	 * @param bccAccount 密送人
	 * @param subject 邮件主题
	 * @param txtContent 邮件内容
	 */
	public void sendTextMail(String[] toAccount, String[] ccAccount, String[] bccAccount, String subject, String txtContent) {
		if (toAccount == null || toAccount.length == 0) {
			return;
		}

		if (ccAccount == null) {
			ccAccount = new String[] {};
		}
		if (bccAccount == null) {
			bccAccount = new String[] {};
		}
		
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			mailMessage.setFrom(fromAccount);
			mailMessage.setTo(toAccount);
			mailMessage.setCc(ccAccount);
			mailMessage.setBcc(bccAccount);
			mailMessage.setSubject(subject);
			mailMessage.setText(txtContent);

			mailSender.send(mailMessage);

			log.info("发送纯文本内容邮件成功, 发件人：" + fromAccount + ", 接收人：" + Arrays.asList(toAccount) + ", 抄送人：" + Arrays.asList(ccAccount) + ", 密送人：" + Arrays.asList(bccAccount));
		} catch (Exception e) {
			log.error("发送纯文本内容邮件失败：", e);
		}
	}
	
	
	/**
	 * 发送HTML内容邮件
	 * @param toAccount 收件人
	 * @param ccAccount 抄送人
	 * @param bccAccount 密送人
	 * @param subject 邮件主题
	 * @param htmlContent 邮件内容
	 */
	public void sendHtmlMail(String[] toAccount, String[] ccAccount, String[] bccAccount, String subject, String htmlContent) {
		if (toAccount == null || toAccount.length == 0) {
			return;
		}

		if (ccAccount == null) {
			ccAccount = new String[] {};
		}
		if (bccAccount == null) {
			bccAccount = new String[] {};
		}
		
		try {
			MimeMessage mmMsg = mailSender.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(mmMsg);
			messageHelper.setFrom(fromAccount);
			messageHelper.setTo(toAccount);
			messageHelper.setCc(ccAccount);
			messageHelper.setBcc(bccAccount);
			messageHelper.setSubject(subject);
			messageHelper.setText(htmlContent, true);

			mailSender.send(mmMsg);

			log.info("发送HTML内容邮件成功, 发件人：" + fromAccount + ", 接收人：" + Arrays.asList(toAccount) + ", 抄送人：" + Arrays.asList(ccAccount) + ", 密送人：" + Arrays.asList(bccAccount));
		} catch (Exception e) {
			log.error("发送HTML内容邮件失败：", e);
		}
	}
	
	
	/**
	 * 发送带附件邮件
	 * @param toAccount 收件人
	 * @param ccAccount 抄送人
	 * @param bccAccount 密送人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param isHtmlContext 邮件内容是否是html格式
	 * @param file 附件
	 */
	public void sendFileMail(String[] toAccount, String[] ccAccount, String[] bccAccount, String subject, String content, boolean isHtmlContext, File file) {
		if (toAccount == null || toAccount.length == 0) {
			return;
		}

		if (ccAccount == null) {
			ccAccount = new String[] {};
		}
		if (bccAccount == null) {
			bccAccount = new String[] {};
		}
		
		try {
			MimeMessage mmMsg = mailSender.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(mmMsg, true);
			messageHelper.setFrom(fromAccount);
			messageHelper.setTo(toAccount);
			messageHelper.setCc(ccAccount);
			messageHelper.setBcc(bccAccount);
			messageHelper.setSubject(subject);
			messageHelper.setText(content, isHtmlContext);
			
			if (file != null) {
				messageHelper.addAttachment(file.getName(), file);
			}

			mailSender.send(mmMsg);

			log.info("发送带附件邮件成功, 发件人：" + fromAccount + ", 接收人：" + Arrays.asList(toAccount) + ", 抄送人：" + Arrays.asList(ccAccount) + ", 密送人：" + Arrays.asList(bccAccount));
		} catch (Exception e) {
			log.error("发送带附件邮件失败：", e);
		}
	}
	
	
	public static void main(String[] args) {
		EmailSender service = new EmailSender();
		
		service.sendTextMail(new String[]{"liaozuliang1988@126.com"}, null, null,  "纯文本内容邮件", "纯文本内容邮件测试！");
		service.sendHtmlMail(new String[]{"liaozuliang1988@126.com"}, null, null, "HTML内容邮件", "<html><head></head><body><h1><font color='blue'>HTML内容邮件！</font></h1></body></html>");
		service.sendFileMail(new String[]{"liaozuliang1988@126.com"}, null, null, "带附件邮件", "<html><head></head><body><h1><font color='blue'>带附件邮件！</font></h1></body></html>", true, new File("E:\\桌面临时文件\\JavaMail邮件\\JavaMail邮件\\SendMail.java"));
	}
	
}
