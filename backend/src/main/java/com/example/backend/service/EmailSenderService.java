package com.example.backend.service;

import com.example.backend.exceptions.EmailNotSend;
import com.example.backend.model.Speler;
import com.example.backend.model.Wedstrijd;
import com.example.backend.utils.LocalDateTimeFormatter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Service
public class EmailSenderService {



    private final JavaMailSender mailSender;

    static final String EMAIL_ADRES_WEDSTRIJD = "mamajosi.esports@gmail.com";
    static final String EMAIL_ONDERWERP_WEDSTRIJD = "Aankondiging wedstrijd";
    static final String EMAIL_BODY_WEDSTRIJD_PREFIX = "Beste,\n\nOp ";
    static final String EMAIL_BODY_WEDSTRIJD_TRAILING = " is er een wedstrijd ingepland waar u deel van uitmaakt." +
                                                                "\n\nMet vriendelijke groeten, " +
                                                                "\n\nHet esports team";

    public EmailSenderService() {
        this.mailSender = getJavaMailSender();
    }

    public void sendMail(@NonNull String emailZender, @NonNull String emailOntvanger,@NonNull String onderwerp, @NonNull String body){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        try {

            if(EmailValidator.getInstance()
                    .isValid(emailZender)){
                mailMessage.setFrom(emailZender);
            }else {
                throw new IllegalArgumentException("email van zender is ongeldig");
            }
            if(EmailValidator.getInstance()
                    .isValid(emailOntvanger)){
                mailMessage.setTo(emailOntvanger);
            }else {
                throw new IllegalArgumentException("email ontvanger is ongeldig");
            }
            mailMessage.setSubject(onderwerp);
            mailMessage.setText(body);
            System.out.println("email wordt verzonden naar: "+emailOntvanger);
            mailSender.send(mailMessage);
            System.out.println("email (met onderwerp: "+ onderwerp + ") is met succes verzonden naar: "+emailOntvanger);
        }catch (Exception e){
            e.printStackTrace();
            throw new EmailNotSend("email (met onderwerp: "+ onderwerp + ") NIET verzonden naar: "+emailOntvanger);
        }

    }

    public void sendEmailNaarSpelersVoorWedstrijd(List<Speler> spelers, Wedstrijd w){
        try{
            for (Speler s:spelers) {
                //datum en tijd formatteren
                String datum = LocalDateTimeFormatter.formatDate(w.getTijdstip());
                String tijd = LocalDateTimeFormatter.formatTime(w.getTijdstip());

                //regex
                var regexBody = EMAIL_BODY_WEDSTRIJD_PREFIX + datum + " om "+ tijd + EMAIL_BODY_WEDSTRIJD_TRAILING;
                sendMail(EMAIL_ADRES_WEDSTRIJD, s.getEmail(), EMAIL_ONDERWERP_WEDSTRIJD, regexBody);
            }
        }catch (EmailNotSend e){
            throw new EmailNotSend("emails versturen naar spelers van wedstrijd mislukt");
        }

    }


    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("mamajosi.esports@gmail.com");
        mailSender.setPassword("peperkoekenhuisje");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
