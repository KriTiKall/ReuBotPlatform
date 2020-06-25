package view.discord.сommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import view.discord.bot.DiscordBotView;
import view.discord.refac.RefacDiscord;


public class Subscribe extends ListenerAdapter {
    private DiscordBotView bot;
    public Subscribe(DiscordBotView bot) { this.bot = bot; }

    final String EMOJI_CRINGE = "<:wideCringe1:623115706627522562><:wideCringe2:623115648637337600>";
    final String EMOJI_TRIDANCE = "<a:TriDance:713920347665334353>";

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String[] args = e.getMessage().getContentRaw().split("\\s+");
        TextChannel channel = e.getTextChannel();

        boolean isAdminAndNotBot = e.getMember().hasPermission(Permission.ADMINISTRATOR) && !e.getMember().getUser().isBot();

        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "submsg") && isAdminAndNotBot) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(0xFFFEEC);
            eb.setTitle("__ОФОРМЛЯЕМ ПОДПИСОЧКУ__");
            eb.addField("", "**\uD83D\uDDFF ЗДЕСЬ ОТПРАВЛЯЕТСЯ РАСПИСАНИЕ**", false); // 🗿
            eb.addField("**Все что нужно и ненужно знать**",
                    "**•** Роль для подписки: " + SetSubRole.subrole +
                    "\n**•** Для подключения/отключения роли нажать на ☑" +
                    "\n**•** Слежу за обновлениями расписания" +
                    "\n**•** Обитаю также в ***телеграме и ВК***" +
                    "\n**•** Ебашу мефедрон 24/7" +
                    "\n**•** Сайт **РЭУ** делали **гомики**" +
                    "\n**•** Любитель ворда и экселя " + EMOJI_CRINGE +
                    "\n**•** Напиши __" + RefacDiscord.prefix + "alhamdulillah__" +
                    "\n**•** Автор — сука" + EMOJI_TRIDANCE,
                    false);
            eb.setImage(RefacDiscord.LINK_KAMPUTER_GIF);

            channel.sendMessage(eb.build()).complete().addReaction("☑").queue();
        }
    }

    final String MESSAGE_ID = "725750117189025804";
    private String userMessage;
    private String subroleName;
    private Member member;
    private boolean isNotBot;
    private Role role;

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        userMessage = e.getMessageId();
        isNotBot = !e.getMember().getUser().isBot();
        member = e.getMember();
        subroleName = e.getGuild().getRoleById(SetSubRole.subroleID).getName();
        role = e.getGuild().getRolesByName(subroleName, false).get(0);

        if (userMessage.equals(MESSAGE_ID) && e.getReactionEmote().getName().equals("☑") && isNotBot)
            e.getGuild().addRoleToMember(member, role).queue();
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        userMessage = e.getMessageId();
        isNotBot = !e.getMember().getUser().isBot();
        member = e.getMember();
        subroleName = e.getGuild().getRoleById(SetSubRole.subroleID).getName();
        role = e.getGuild().getRolesByName(subroleName, false).get(0);

        if (userMessage.equals(MESSAGE_ID) && e.getReactionEmote().getName().equals("☑") && isNotBot)
            e.getGuild().removeRoleFromMember(member, role).queue();
    }

    // все то что ниже недоделано да и в ходе переговоров оказалось и вовсе ненужным, а удалять то жалко =(

    /*
     * String[] info
     * info[0]: наличие/отсутствие подписки
     * info[1]: подписаться/отписаться
     * info[2]: реакция
     *
     * в зависимости от включенной/выключенной подписки
     * массив будет хранить соответствущие значения
     */

//    public void onMessageReceived2(MessageReceivedEvent e) {
//        String[] args = e.getMessage().getContentRaw().split("\\s+");
//        String[] info = new String[3];
//        int color;
//        TextChannel channel;
//        Member member;
//
//        if (args[0].equalsIgnoreCase(RefacDiscord.prefix + "sub")) {
//            channel = e.getTextChannel();
//            member = e.getMember();
//            Role role = e.getGuild().getRoleById(SetSubRole.subroleID);
//
//            if (!member.getRoles().contains(role)) {
//                color = 0xff3300;
//                info[0] = "Отключена";
//                info[1] = "подписаться";
//                info[2] = "☑";
//            } else {
//                color = 0x8ce482;
//                info[0] = "Включена";
//                info[1] = "отписаться";
//                info[2] = "❌";
//            }
//            embedMessage2(info, color, channel, member);
//        }
//    }
//
//    private void embedMessage2(String[] info, int color, TextChannel channel, Member member) {
//        EmbedBuilder eb = new EmbedBuilder();
//
//        eb.setColor(color);
//        eb.setDescription("Состояние подписки: **" + info[0] + "**");
//        eb.addField("————————————————————————",
//                "Чтобы " + info[1] + ", нажмите на реакцию снизу " + info[2],
//                false);
//        eb.setFooter("Requested by " + member.getUser().getAsTag(), member.getUser().getAvatarUrl());
//
//        channel.sendMessage(eb.build()).complete().addReaction(info[2]).queue();
//    }
}
