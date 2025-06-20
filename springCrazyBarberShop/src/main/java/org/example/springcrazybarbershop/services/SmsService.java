package org.example.springcrazybarbershop.services;

import lombok.extern.slf4j.Slf4j;
import org.example.springcrazybarbershop.exceptions.SmsException;
import org.example.springcrazybarbershop.services.interfeces.ISmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class SmsService implements ISmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private final String smsUrl;
    private final String apiId;
    private final RestClient restClient;

    public SmsService(
            @Value("${sms.ru.url}") String smsUrl,
            @Value("${sms.ru.api_id}") String apiId,
            RestClient.Builder restClientBuilder) {
        this.smsUrl = smsUrl;
        this.apiId = apiId;
        this.restClient = restClientBuilder.build();
    }

    @Override
    public void sendSms(String phone, String text) throws SmsException {
        try {
            logger.debug("Подготовка к отправке SMS на номер: {}", phone);
            logger.debug("Текст сообщения: {}", text);
            
            String url = smsUrl + "?api_id=" + apiId + "&to=" + phone + "&msg=" + text + "&json=1";
            logger.debug("URL запроса: {}", url);

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
            logger.info("SMS успешно отправлено. Ответ сервера: {}", response);
            
        } catch (RestClientException e) {
            logger.error("Ошибка при отправке SMS на номер {}: {}", phone, e.getMessage(), e);
            throw new SmsException("Failed to send SMS to " + phone);
        } catch (Exception e) {
            logger.error("Неожиданная ошибка при отправке SMS на номер {}: {}", phone, e.getMessage(), e);
            throw new SmsException("Unexpected error while sending SMS to " + phone);
        }
    }
}
