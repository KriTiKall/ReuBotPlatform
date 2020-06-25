package view.discord.bot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import view.BotView;
import view.discord.refac.RefacDiscord;
import view.discord.сommands.GetTT;
import view.discord.сommands.SetSubRole;
import view.discord.сommands.Subscribe;

public class DiscordBotView implements BotView {
    public static JDA jda;

    private static DiscordBotView view;

    public static DiscordBotView getInstance() {
        if (view == null) view = new DiscordBotView();
        return view;
    }
    private DiscordBotView() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(RefacDiscord.TOKEN)
                    .build()
                    .awaitReady();
            jda.getPresence().setStatus(OnlineStatus.ONLINE);
            jda.getPresence().setActivity(Activity.watching("расписание каждые 10 минут"));

            jda.addEventListener(new GetTT(this)); // получать расписание по команде
            jda.addEventListener(new Subscribe(this)); // подписаться/отписаться
            jda.addEventListener(new SetSubRole(this)); // поменять роль для подписки
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //public static void main(String[] args) { BotView bot = new DiscordBotView(); }
    // отправка сабам расписания
    final String CHANNEL_ID = "725745635650699417";
    public void sendTimetableForSubs(String[][] timetable) {
        TextChannel channel = jda.getTextChannelById(CHANNEL_ID);
        channel.sendMessage("**кушаем сладенькое расписание** \uD83D\uDC26 \n||для получения подписки смотреть закрепленное сообщение..||"); //🐦
        embedTimetable(timetable, channel);
    }
    // эмбед с расписанием
    public void embedTimetable(String[][] timetable, TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        String date = timetable[0][1];
        String reaIcon = RefacDiscord.LINK_REA_ICON;
        String gorin = RefacDiscord.LINK_GORIN;

        eb.setAuthor("\uD83D\uDCDA РАСПИСАНИЕ"); // 📚
        eb.setThumbnail(reaIcon);
        eb.setTitle(date, RefacDiscord.LINK_REA_PK_21);
        for (int i = 1; i < timetable.length; i++)
            setLesson(eb, timetable[i][0], timetable[i][1]);
        eb.setFooter("как всегда говно", gorin);

        channel.sendMessage(eb.build()).queue();
    }
    // создает поле для каждой пары
    private void setLesson(EmbedBuilder eb, String time, String lesson) {
        eb.addField("`" + time + "`", lesson, false);
    }
}
