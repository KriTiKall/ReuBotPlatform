package view.discord.сommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import view.discord.bot.DiscordBotView;
import view.discord.refac.RefacDiscord;

public class SetSubRole extends ListenerAdapter {
    private DiscordBotView bot;
    public static String subroleID = "632279900447047708";
    public static String subrole = DiscordBotView.jda.getGuildById(620682596564598797L).getRoleById(subroleID).getAsMention();

    public SetSubRole(DiscordBotView bot) { this.bot = bot; }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] args = e.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = e.getTextChannel();

        subroleID = subrole.replaceAll("\\W+", "");
        //System.out.println(subroleID);

        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "setsubrole") && e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (args.length == 1)
                channel.sendMessage("Саброль на данный момент: " + subrole).queue();

            else if (args.length == 2 && args[1].startsWith("<@&")) {
                //channel.sendMessage("ПРОВЕРКА СВЯЗИ если после этого ничего не произошло то видимо это не работает").queue();
                //channel.sendMessage("Саброль изменена на: " + e.getGuild().getRoleById(subroleID).getAsMention()).queue();
                subrole = args[1];
                channel.sendMessage("Саброль изменена на: " + subrole).queue();
            }

            else
                channel.sendMessage("хуйню написал").queue();
        }
        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "setsubrole") && !e.getMember().hasPermission(Permission.ADMINISTRATOR))
            channel.sendMessage("хуй я тебе отвечу слышишь").queue();
    }
}
