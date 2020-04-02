package view;

import view.bot.DiscordBotView;

public interface BotView {
    DiscordBotView sendTimetable(String[][] timetable);
}
