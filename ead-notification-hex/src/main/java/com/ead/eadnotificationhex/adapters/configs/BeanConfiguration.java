package com.ead.eadnotificationhex.adapters.configs;

import com.ead.eadnotificationhex.EadNotificationHexApplication;
import com.ead.eadnotificationhex.core.ports.NotificationPersistencePort;
import com.ead.eadnotificationhex.core.ports.NotificationServicePort;
import com.ead.eadnotificationhex.core.services.NotificationServicePortImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EadNotificationHexApplication.class)
public class BeanConfiguration {

    @Bean
    NotificationServicePortImpl notificationServicePortImpl(NotificationPersistencePort persistencePort){
        return new NotificationServicePortImpl(persistencePort);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
