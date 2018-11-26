package club.sk1er.mods.victory_royale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;
import pw._2pi.autogg.victory_royale.gg.AutoGG;

import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;


@Mod(modid = "victory_royale", version = "1.0", clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
public class VictoryRoyale {

    private static VictoryRoyale INSTANCE;
    int e = 0;
    private Sk1erMod sk1erMod;
    private boolean show = false;
    private ResourceLocation textureLoc = new ResourceLocation("victory_royale", "victory_royale.png");
    private ResourceLocation soundLoc = new ResourceLocation("victory_royale", "victory_royale");
    private long start = 0;
    private ConcurrentLinkedQueue<WhiteLine> points = new ConcurrentLinkedQueue<>();
    private IntBuffer pixelBuffer;
    private int[] pixelValues;
    private long lastFrameTime = 0;

    public static VictoryRoyale getInstance() {
        return INSTANCE;
    }

    @Mod.EventHandler
    public void init(final FMLPreInitializationEvent event) {
        INSTANCE = this;
        sk1erMod = new Sk1erMod("victory_royale", "1.0", "Victory Royale");
        sk1erMod.checkStatus();
        new AutoGG(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(this);


    }


    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        long diff = System.currentTimeMillis() - start;

        if (diff < 10000) {

            double v1 = 2000;
            if (diff < v1) {
                double percentToDone = diff / v1;
                double centerX = resolution.getScaledWidth_double() / 2;
                double centerY = resolution.getScaledHeight_double() / 2;
                double angle = Math.toDegrees(Math.atan(resolution.getScaledHeight_double() / resolution.getScaledWidth_double()));
                for (WhiteLine point : points) {
                    int trueX = (int) (point.xPercent * resolution.getScaledWidth_double());
                    int trueY = (int) (point.yPercent * resolution.getScaledHeight_double());

                    double dx = centerX - trueX;
                    double dy = centerY - trueY;
                    double v = Math.toDegrees(MathHelper.atan2(-dy, dx)) + 180;
                    int side;
                    if (v > 0 && v < angle || v > 360 - angle) {
                        side = 1;
                    } else if (v > angle && v < 180 - angle) {
                        side = 0;
                    } else if (v > 180 - angle && v < 180 + angle) {
                        side = 3;
                    } else {
                        side = 2;
                    }

                    int px;
                    int py;

                    double slope = (centerY - trueY) / (centerX - trueX);
                    //y - y1 = m (x-x1)
                    //y = m (deltaX) + y1
                    if (side == 1 || side == 3) {
                        //solve for when x = right side of the screen
                        int xpos = side == 1 ? resolution.getScaledWidth() : 0;
                        py = (int) (slope * (xpos - centerX) + centerY);
                        px = xpos;
                    } else {
                        py = side != 0 ? resolution.getScaledHeight() : 0;
                        px = (int) ((py - trueY + slope * trueX) / slope);
                    }

                    double dy2 = (trueY - py) - (trueY - py) * percentToDone * .2D;
                    double dx2 = (trueX - px) - (trueX - px) * percentToDone * .2D;

                    trueY = (int) (py + dy2);
                    trueX = (int) (px + dx2);

                    GlStateManager.enableTexture2D();
                    GlStateManager.enableBlend();
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

                    float colorAlpha = percentToDone < .5D ? 1F : (float) (1.0F - percentToDone) * 2;
                    GlStateManager.enableAlpha();
                    GlStateManager.color(1, 1, 1, colorAlpha);
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
                    if (side == 1) {
                        worldrenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldrenderer.pos((double) px + 0, (double) py + point.width, 0.0D).endVertex();
                        worldrenderer.pos((double) px, (double) py, 0.0D).endVertex();
                    } else if (side == 3) {
                        worldrenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldrenderer.pos((double) px, (double) py, 0.0D).endVertex();
                        worldrenderer.pos((double) px + 0, (double) py + point.width, 0.0D).endVertex();
                    } else if (side == 2) {
                        worldrenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldrenderer.pos((double) px, (double) py, 0.0D).endVertex();
                        worldrenderer.pos((double) px + 10, (double) py, 0.0D).endVertex();
                    } else {
                        worldrenderer.pos((double) trueX, (double) trueY, 0.0D).endVertex();
                        worldrenderer.pos((double) px + 10, (double) py, 0.0D).endVertex();
                        worldrenderer.pos((double) px, (double) py, 0.0D).endVertex();
                    }

                    tessellator.draw();
                    GlStateManager.enableTexture2D();


                }
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int i = resolution.getScaledWidth() / 4;
            Minecraft.getMinecraft().getTextureManager().bindTexture(textureLoc);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();

            int i1 = 9000;
            float fadeOutAlpha = 1;
            if (diff > i1) {
                double l = (diff - i1) / 1000D;
                fadeOutAlpha = (float) (1 - l);
            }

            GlStateManager.color(1, 1, 1, diff < 1000 ? (float) Math.pow(diff / 1000D, 2) : fadeOutAlpha);
            GlStateManager.translate(i, 10, 0);
            Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1200, 675, i * 2, i, 1200, 675);
            GlStateManager.popMatrix();
        }

    }

    public void gameEnded() {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        if (thePlayer.isInvisible() || thePlayer.isInvisibleToPlayer(thePlayer)) {
            return;
        }
        for (PotionEffect potionEffect : thePlayer.getActivePotionEffects()) {
            if (potionEffect.getPotionID()==14) {
                return;
            }
        }
        thePlayer.playSound(soundLoc.toString(), .5F, 1.0F);
        start = System.currentTimeMillis();
        points.clear();
        for (int i = 0; i < 15; i++) {
            ThreadLocalRandom current = ThreadLocalRandom.current();
            points.add(new WhiteLine(current.nextDouble(1.0), current.nextDouble(1.0), current.nextInt(5)));
        }
    }


    class WhiteLine {

        private double xPercent, yPercent;
        private int width;

        public WhiteLine(double xPercent, double yPercent, int width) {
            this.xPercent = xPercent;
            this.yPercent = yPercent;
            this.width = width;
        }
    }

}
