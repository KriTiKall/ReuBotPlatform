package view.vk.bot;

import com.petersamokhin.bots.sdk.clients.Group;
import com.petersamokhin.bots.sdk.objects.Message;
import view.BotView;
import view.vk.refac.MockObject;
import view.vk.refac.RefacVk;

public class VkBotView implements BotView {

    private static Group group;

    private static int chatid;

    private String[][] timetable = MockObject.mock_1;

    private static VkBotView view;

    public static VkBotView getInstance() {
        if(view == null)
            view = new VkBotView();
        return view;
    }

    private VkBotView() {
        group = new Group(7240343, RefacVk.ACCESS_TOKEN);
        group.onSimpleTextMessage(VkBotView::onTextMessage);
    }

    private static void onTextMessage(Message message) {
        String[] text = message.getText().split(" ");

        chatid = message.authorId();

        if (text[0].equalsIgnoreCase("/getLesson")) {
            view.sendTimetableForSubs(view.timetable);
        }
    }

    @Override
    public void sendTimetableForSubs(String[][] timetable) {
        new Message().from(group)
                .to(chatid)
                .text(timetableToString(timetable))
                .send();
    }

    private String timetableToString(String[][] timetable) {
        StringBuilder res = new StringBuilder();

        for(int i = 0; i < 9; i++){
            res.append(String.format(RefacVk.template[i], timetable[i][1]));
            if(i == 0)
                res.append("\n");
        }

        return new String(res);
    }

    public void setTimetable(String[][] timetable) {
        this.timetable = timetable;
    }

    public String[][] getTimetable() {
        return timetable;
    }
}
