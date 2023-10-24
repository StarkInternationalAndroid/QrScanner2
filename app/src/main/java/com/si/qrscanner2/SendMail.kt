package com.si.qrscanner2

import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.io.PrintWriter
import java.io.StringWriter

object GmailCredentials {
    val EMAIL: String = "starksm64@gmail.com"
    val PASSWORD: String = BuildConfig.GMAIL_ANDROID_PASS
}

fun sendEmail(to: String,
              from: String = "starksm@starkinternational.com",
              subject: String = "test email",
              body : String = "test email body"
): String {
    val props = System.getProperties()
    props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.socketFactory.port", "465")
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.port", "465")

    val session =  Session.getInstance(props,
        object : Authenticator() {
            //Authenticating the password
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(GmailCredentials.EMAIL, GmailCredentials.PASSWORD)
            }
        })

    var status: String = "Ok"
    try {
        //Creating MimeMessage object
        val mm = MimeMessage(session)
        val emailId = to
        //Setting sender address
        mm.setFrom(InternetAddress(from))
        //Adding receiver
        mm.addRecipient(
            Message.RecipientType.TO,
            InternetAddress(emailId)
        )
        //Adding subject
        mm.subject = subject
        //Adding message
        mm.setText(body)

        //Sending email
        Transport.send(mm)
    } catch (e: MessagingException) {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        status = sw.toString()
    } catch (e: Exception) {
        status = e.toString()
    }
    return status
}