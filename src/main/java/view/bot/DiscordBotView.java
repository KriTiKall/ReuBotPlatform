package view.bot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import view.BotView;
import view.bot.discordCommands.GetTT;
import view.refacs.RefacDiscord;

public class DiscordBotView implements BotView {
    private static JDA jda;

    public DiscordBotView() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(RefacDiscord.token)
                    .build()
                    .awaitReady();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.watching("расписание каждые 5 минут"));

            jda.addEventListener(new GetTT(this)); // получать расписание по команде
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BotView bot = new DiscordBotView();
    }

    public DiscordBotView sendTimetable(String[][] timetable) {
        TextChannel channel = jda.getTextChannelById("626093012317110273");
        EmbedBuilder eb = new EmbedBuilder();
        String date = timetable[0][1];
        String reaIcon = "https://cdn.discordapp.com/attachments/626093012317110273/695312495585263646/D093D0B5D180D0B1_D0A0D0ADD0A3_D0B8D0BC_D09FD0BBD0B5D185D0B0D0BDD0BED0B2D0B0.png";
        String gorin = "https://cdn.discordapp.com/attachments/620682597076566037/695317901405847571/-LGs1pvZfrE.jpg";

        eb.setAuthor("\uD83D\uDCDA РАСПИСАНИЕ"); // 📚
        eb.setThumbnail(reaIcon);
        eb.setTitle(date, "http://www.rea.perm.ru/?page_id=1036&id=Timetable/rs_PKo-21");
        for (int i = 1; i < timetable.length; i++)
            setLesson(eb, timetable[i][0], timetable[i][1]);
        eb.setFooter("как всегда говно", gorin);

        channel.sendMessage(eb.build()).queue();
        return null;
    }

    private void setLesson(EmbedBuilder eb, String time, String lesson) {
        eb.addField("`" + time + "`", lesson, false);
    }
}
