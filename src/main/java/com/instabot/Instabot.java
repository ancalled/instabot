package com.instabot;

import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.DbServiceImpl;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Instabot {

    private static Logger log = Logger.getLogger(Instabot.class);
    private static String APP_HOME = System.getProperty("user.dir");
    private static String APP_PROP = APP_HOME + File.separator + "bot.properties";

    private InstaService instaService;
    private ViaphoneService viaphoneService;
    private DbService dbService;

    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(APP_PROP);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }

        Instabot bot = new Instabot(prop);

        log.info("Starting instabot...");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        MediaThread mediaThread = new MediaThread(bot.instaService, bot.dbService);
        OrdersThread ordersThread = new OrdersThread(bot.instaService, bot.viaphoneService, bot.dbService);
        AuthorizeThread authThread = new AuthorizeThread(bot.instaService, bot.viaphoneService, bot.dbService);
        ConfirmThread confirmThread = new ConfirmThread(bot.instaService, bot.viaphoneService, bot.dbService);

        scheduler.scheduleAtFixedRate(mediaThread, 0, 10, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(ordersThread, 0, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(authThread, 0, 5, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(confirmThread, 0, 5, TimeUnit.MINUTES);
    }

    public Instabot(Properties prop) {
        dbService = new DbServiceImpl(prop);
        instaService = new InstaService();
        viaphoneService = new ViaphoneService();
    }
}
