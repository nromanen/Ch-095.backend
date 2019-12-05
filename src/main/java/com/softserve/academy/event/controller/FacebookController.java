package com.softserve.academy.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/facebook")
public class FacebookController {

    private Facebook facebook;

    @Autowired
    public FacebookController(Facebook facebook) {
        this.facebook = facebook;
    }

    @GetMapping
    public String getFacebookFeeds(Model model){
    //        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
    //            return "redirect:/connect/facebook";
    //        }
    //        PagedList<Post> posts = facebook.feedOperations().getPosts();
    //        model.addAttribute("profileName", posts.get(0).getFrom().getName());
    //        model.addAttribute("posts", posts);
    //        return "profile";

        if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }

        model.addAttribute(facebook.userOperations().getUserProfile());
        PagedList homeFeed = facebook.feedOperations().getHomeFeed();
        model.addAttribute("feed", homeFeed);
        return "feeds";
    }
}
