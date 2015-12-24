package com.instabot;

import com.instabot.models.*;
import com.instabot.service.DbService;
import com.instabot.service.impl.InstaService;
import org.apache.log4j.Logger;

import java.util.List;

import static com.instabot.utils.Constants.*;

public class MediaTask implements Runnable {

    private static Logger log = Logger.getLogger(MediaTask.class);

    private InstaService instaService;
    private DbService dbService;

    public MediaTask(InstaService instaService, DbService dbService) {
        this.instaService = instaService;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            log.info("Starting scan medias");
            scanMediaToSell();
            log.info("Finished scaning medias");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private void scanMediaToSell() {
        List<Post> posts = instaService.getRecentTaggedMedia(SELL_TAG);
        for (Post p : posts) {
            Post oldPost = dbService.getPost(p.getPostId());
            if (oldPost == null) {
                log.info("Found new post to sell: " + p.toString());
                Long id = dbService.createPost(p);
                if (id != null) log.info("Post with id: " + id + " inserted successfully to db!");
            }
        }
    }
}