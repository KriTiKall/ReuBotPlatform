package view.bot;

import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import view.BotView;
import view.refacs.RefacVk;

public class VkBotView implements BotView {

    private static final Group group = new Group((int) RefacVk.GROUP_ID, RefacVk.ACCESS_TOKEN);

    private String[][] timetable;

    public VkBotView getInstance(String[][] timetable) {
        return new VkBotView(timetable);
    }

    private VkBotView(String[][] timetable) {
        this.timetable = timetable;
        init();
    }

    @Override
    public void init() {
        group.onSimpleTextMessage(VkBotView::onTextMessage);
    }

    @Override
    public void sendTimetable(String[][] timetable) {

    }

    private static void onTextMessage(Message message) {
        String[] text = message.getText().split(" ");
        if (text[0].equalsIgnoreCase("/getLesson")) {
            sendMessage("We are Russians with us GOD", message);
        }
    }

    private void sendMessagewithTimetable(String[][] timetable, Message msg) {


    }

    private static void sendMessage(String text, Message msg) {
        new Message().from(group)
                .to(msg.authorId())
                .text(text)
                .send();
    }
}
