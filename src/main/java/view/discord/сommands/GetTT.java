package view.discord.сommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import view.discord.bot.DiscordBotView;
import view.discord.refac.RefacDiscord;

import java.awt.*;

public class GetTT extends ListenerAdapter {
    public static final String[][] tt = {
            {"номер пары", "ВТ 17.03.2020"},
            {"1 пара 8:30 - 10:00", ""},
            {"2 пара 10:10 - 11:40", "Компьютерная графика преподаватель, Серебрякова Н. А., лекция"},
            {"3 пара 12:10 - 13:40", "Компьютерная графика преподаватель, Серебрякова Н. А., лекция"},
            {"4 пара 13:50 - 15:20", "Компьютерная графика преподаватель, Серебрякова Н. А., лекция"},
            {"5 пара 15:30 - 17:00", ""},
            {"6 пара 17:30 - 19:00", ""},
            {"7 пара 19:10 - 20:40", ""},
            {"8 пара 20:45 - 22:15", ""}
    };

    private DiscordBotView bot;

    public GetTT(DiscordBotView bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] args = e.getMessage().getContentRaw().split("\\s+");
        boolean userNotBot = !e.getMember().getUser().isBot();
        // отправит расписание при написании r/getTT
        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "getTT") && userNotBot) {
            TextChannel channel = e.getTextChannel();
            channel.sendTyping();
            bot.embedTimetable(tt, channel);
        }
        // тестовая команда
        if (args[0].equalsIgnoreCase("hooi") && userNotBot)
            e.getChannel().sendMessage("ИДИ НАХУЙ").queue();

        if (args[0].equalsIgnoreCase("ИДИ") && args[1].equalsIgnoreCase("НАХУЙ") && userNotBot)
            e.getChannel().sendMessage("САМ ИДИ НАХУЙ").queue();

        final String ALHAMDULILLAH = "https://tenor.com/tEh1.gif";

        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "alhamdulillah") && userNotBot) {
            EmbedBuilder eb = new EmbedBuilder();
            e.getChannel().sendMessage(ALHAMDULILLAH).queue();
        }
    }

}
