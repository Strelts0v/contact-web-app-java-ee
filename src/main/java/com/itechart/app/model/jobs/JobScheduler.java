package com.itechart.app.model.jobs;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class JobScheduler {

    private final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    private volatile static JobScheduler INSTANCE;

    private Scheduler scheduler;

    private static final String BIRTHDAY_JOB_NAME = "birthdayJob";
    private static final String BIRTHDAY_JOB_TRIGGER = "birthdayTrigger";
    private static final String BIRTHDAT_JOB_GROUP = "birthdayGroup";

    private JobScheduler(){
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException se){
            logger.error(se.getMessage());
        }
    }

    public static JobScheduler getInstance(){
        if(INSTANCE == null){
            synchronized (JobScheduler.class){
                if(INSTANCE == null){
                    INSTANCE = new JobScheduler();
                }
            }
        }
        return INSTANCE;
    }

    public void initializeJobScheduler(){
        try {
            // define the job and tie it to BirthdayJob class
            JobDetail jobDetail = newJob(BirthdayJob.class)
                    .withIdentity(BIRTHDAY_JOB_NAME, BIRTHDAT_JOB_GROUP)
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(BIRTHDAY_JOB_TRIGGER, BIRTHDAT_JOB_GROUP)
                    .startNow()
                    .withSchedule(simpleSchedule()
                        .withIntervalInHours(24)
                        .repeatForever())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            scheduler.start();
        }catch (SchedulerException se){
            logger.error(se.getMessage());
        }
    }
}
