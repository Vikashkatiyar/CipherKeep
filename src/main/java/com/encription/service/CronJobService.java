package com.encription.service;

import com.encription.constants.ApplicationConstants;
import com.encription.model.Secret;
import com.encription.repository.SecretRepository;
import com.encription.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CronJobService {

    @Autowired
    private SecretRepository secretRepository;

    @Scheduled(cron = "1 0 0 * * ?")
    public void reEncryptSecrets() {
        List<Secret> secrets = fetchSecrets();
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);

        for (Secret secret : secrets) {
            if (secret.getLastModified().isBefore(tenDaysAgo)) {
                reEncrypt(secret);
            }
        }
    }

    private List<Secret> fetchSecrets() {
        return secretRepository.findAll();
    }

    private void reEncrypt(Secret secret) {
        String decryptedData= EncryptionUtil.decrypt(secret.getEncryptedData(), ApplicationConstants.SECRET_KEY);
        secret.setEncryptedData(EncryptionUtil.encrypt(decryptedData,ApplicationConstants.SECRET_KEY));
    }

}
