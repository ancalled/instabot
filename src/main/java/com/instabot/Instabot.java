package com.instabot;

import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import com.instabot.service.impl.DbServiceImpl;
import com.instabot.service.impl.ViaphoneService;
import org.apache.log4j.Logger;

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

        int periodMedia = 5;
        int periodOrders = 2;
        int periodAuth = 2;
        int periodConfirm = 2;

        Properties prop = new Properties();
        try {
            InputStream input = new FileInputStream(APP_PROP);
            prop.load(input);
            periodMedia = Integer.parseInt(prop.getProperty("period.media"));
            periodOrders = Integer.parseInt(prop.getProperty("period.orders"));
            periodAuth = Integer.parseInt(prop.getProperty("period.auth"));
            periodConfirm = Integer.parseInt(prop.getProperty("period.confirm"));
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }

        Instabot bot = new Instabot(prop);

        log.info("Starting instabot...");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        MediaTask media = new MediaTask(bot.instaService, bot.dbService);
        OrdersTask orders = new OrdersTask(bot.instaService, bot.viaphoneService, bot.dbService);
        AuthorizeTask auth = new AuthorizeTask(bot.instaService, bot.viaphoneService, bot.dbService);
        ConfirmTask confirm = new ConfirmTask(bot.instaService, bot.viaphoneService, bot.dbService);

        scheduler.scheduleAtFixedRate(media, 0, periodMedia, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(orders, 0, periodOrders, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(auth, 0, periodAuth, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(confirm, 0, periodConfirm, TimeUnit.MINUTES);
    }

    public Instabot(Properties prop) {
        dbService = new DbServiceImpl(prop);
        instaService = new InstaService();
        viaphoneService = new ViaphoneService();
    }
}
