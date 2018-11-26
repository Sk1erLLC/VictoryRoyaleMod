package pw._2pi.autogg.victory_royale.gg;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class VictoryRoyaleCommand extends CommandBase {
    public String getCommandName() {
        return "victory_royale";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canSenderUseCommand(final ICommandSender sender) {
        return true;
    }

    public void processCommand(final ICommandSender sender, final String[] args) {
        if (args.length == 0 || args.length > 2) {
            this.showSyntaxError(sender);
            return;
        }
        final String s = args[0];
        switch (s) {

            case "toggle":
            case "t": {
                AutoGG.getInstance().setToggled();
                this.showMessage(EnumChatFormatting.GRAY + "VictoryRoyale: " + (AutoGG.getInstance().isToggled() ? (EnumChatFormatting.GREEN + "On") : (EnumChatFormatting.RED + "Off")), sender);
                AutoGG.getInstance().getUtil().save();
                break;
            }
            default: {
                this.showSyntaxError(sender);
                break;
            }
        }
    }

    public String getCommandUsage(final ICommandSender sender) {
        return "/victory_royale <toggle>";
    }

    private void showMessage(final String message, final ICommandSender sender) {
        sender.addChatMessage((IChatComponent) new ChatComponentText(message));
    }

    private void showSyntaxError(final ICommandSender sender) {
        this.showMessage(EnumChatFormatting.RED + "Usage: " + this.getCommandUsage(sender), sender);
    }

    private void showError(final String error, final ICommandSender sender) {
        this.showMessage(EnumChatFormatting.RED + "Error: " + error, sender);
    }
}
