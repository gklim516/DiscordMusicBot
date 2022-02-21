package com.glim.bot;

import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();
        Guild eventGuild = event.getGuild();
        Member user = event.getMember();
        Member bot = eventGuild.getSelfMember();
        MessageChannel channel = event.getChannel();

        if (content.equals("!status")) {
            channel.sendMessage("I am online!").queue();
        } else if (content.startsWith("!play")) {
            //Checks if user is in any voice channel
            if (!user.getVoiceState().inAudioChannel()) {
                channel.sendMessage("User is not in any voice channel!").queue();
                return;
            }

            //Checks if both User and Bot are in the same voice channel
            if (!user.getVoiceState().getChannel().equals(bot.getVoiceState().getChannel()) && bot.getVoiceState().inAudioChannel()) {
                channel.sendMessage("Bot and User are not in the same voice channel!").queue();
                return;
            }

            if (content.split(" ").length < 2) {
                channel.sendMessage("Please provide a number for song category:-")
                        .append(System.lineSeparator())
                        .append("`1 - English Pop songs`")
                        .append(System.lineSeparator())
                        .append("`2 - Chinese Pop songs`")
                        .append(System.lineSeparator())
                        .append("`3 - Japanese Pop songs`")
                        .append(System.lineSeparator())
                        .append("`4 - Classical Piano songs`")
                        .queue();
                return;
            }

            AudioManager audioManager = eventGuild.getAudioManager();
            AudioChannel memberChannel = user.getVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);

            String choice = content.split(" ")[1];
//            if (!isUrl(url)) {
//                url = "ytsearch:" + url;
//            }
            for (int i = 0; i < 5; i++) {
                String url = null;
                try {
                    url = "ytsearch:" + new SpotifyHelper().getSongRecommendations(Integer.parseInt(choice));
                } catch (Exception e) {
                    return;
                }

                PlayerManager.getInstance().loadAndPlay(eventGuild, url, channel);
            }
        } else if (content.equals("!stop")) {
            TrackScheduler trackScheduler = PlayerManager.getInstance().getMusicManager(eventGuild).trackScheduler;
            if (!bot.getVoiceState().inAudioChannel()) {
                channel.sendMessage("Bot is not in any voice channel!").queue();
                return;
            }

            trackScheduler.getAudioPlayer().stopTrack();
            trackScheduler.getQueue().clear();

            channel.sendMessage("Current track is stopped and queue is cleared!");

            AudioManager audioManager = eventGuild.getAudioManager();
            audioManager.closeAudioConnection();
        } else if (content.equals("!skip")) {
            TrackScheduler trackScheduler = PlayerManager.getInstance().getMusicManager(eventGuild).trackScheduler;
            if (trackScheduler.getAudioPlayer().getPlayingTrack() == null) {
                return;
            }
            trackScheduler.nextTrack();
        }
    }

    private boolean isUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException e) {
            return false;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
