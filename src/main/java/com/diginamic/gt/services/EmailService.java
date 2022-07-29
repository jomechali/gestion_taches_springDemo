package com.diginamic.gt.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
