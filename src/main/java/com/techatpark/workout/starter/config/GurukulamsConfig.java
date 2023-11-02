package com.techatpark.workout.starter.config;

import com.gurukulams.core.GurukulamsManager;
import com.gurukulams.notebook.service.AnnotationService;
import com.gurukulams.core.service.LearnerProfileService;
import com.gurukulams.core.service.LearnerService;
import jakarta.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class GurukulamsConfig {

    /**
     * Provides Org Service Instalnce.
     * @param dataSource
     * @return orgService
     */
    @Bean
    GurukulamsManager gurukulamsManager(final DataSource dataSource) {
        return GurukulamsManager.getManager(dataSource);
    }

    /**
     * learnerService.
     * @param gurukulamsManager
     * @param validator
     * @return learnerService
     */
    @Bean
    LearnerService learnerService(final GurukulamsManager gurukulamsManager,
                                  final Validator validator) {
        return new LearnerService(gurukulamsManager, validator);
    }

    /**
     * learnerProfileService.
     * @param gurukulamsManager
     * @return learnerProfileService
     */
    @Bean
    LearnerProfileService learnerProfileService(
            final GurukulamsManager gurukulamsManager) {
        return new LearnerProfileService(gurukulamsManager);
    }


    /**
     * AnnotationService.
     * @return annotationService
     */
    @Bean
    AnnotationService annotationService() {
        return new AnnotationService();
    }

}