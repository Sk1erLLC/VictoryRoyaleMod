package pw._2pi.autogg.victory_royale.gg;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class GGListener
{
    private static String unformattedMessage;
    private Random r;
    
    public GGListener() {
        this.r = new Random();
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent event) {
        if (!AutoGG.getInstance().isHypixel() || !AutoGG.getInstance().isToggled() || AutoGG.getInstance().isRunning() || AutoGG.getInstance().getTriggers().isEmpty()) {
            return;
        }
        GGListener.unformattedMessage = event.message.getUnformattedText();
        GGListener.unformattedMessage = EnumChatFormatting.getTextWithoutFormattingCodes(GGListener.unformattedMessage);
        if (AutoGG.getInstance().getTriggers().stream().anyMatch(trigger -> GGListener.unformattedMessage.contains(trigger.toString())) && GGListener.unformattedMessage.startsWith(" ")) {
            AutoGG.getInstance().setRunning(true);
            AutoGG.THREAD_POOL.submit(new GGThread());
        }
    }
}
