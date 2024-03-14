package com.ikn.ums.admin.utils;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender sender;
	
	public EmailService(JavaMailSender sender) {
		this.sender = sender;
	}

	public boolean sendMail(String to, String subject, String textBody, String[] cc, String[] bcc, MultipartFile file) {

		boolean flag = false;
		//create Mime Message
		MimeMessage message = sender.createMimeMessage();
		//set data to message using helper
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, file!=null);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(textBody);
			if(cc != null) {
				helper.setCc(cc);
			}//if
			if(bcc != null) {
				helper.setBcc(bcc);
			}
			if(file != null) {
				helper.addAttachment(file.getOriginalFilename(), file);
			}
			sender.send(message);
			flag = true;
		}//try
		catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public boolean sendMail(String to, String subject, String textBody) {
		return sendMail(to, subject, textBody, null, null, null);
	}

 

}
