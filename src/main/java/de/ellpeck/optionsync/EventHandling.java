package de.ellpeck.optionsync;

import de.ellpeck.optionsync.gui.GuiConfigureOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EventHandling{

    //Yes, this is my birthday. I really did that.
    private static final int BUTTON_ID = -21051999;

    @SubscribeEvent
    public void onGuiOpened(GuiScreenEvent.InitGuiEvent event){
        if(event.getGui() instanceof GuiOptions){
            String strg = OptionSync.NAME;
            event.getButtonList().add(new GuiButton(BUTTON_ID, 5, event.getGui().height-25, event.getGui().mc.fontRendererObj.getStringWidth(strg)+15, 20, strg));
        }
    }

    @SubscribeEvent
    public void onGuiClicked(GuiScreenEvent.ActionPerformedEvent event){
        if(event.getGui() instanceof GuiOptions && event.getButton() != null && event.getButton().id == BUTTON_ID){
            event.getGui().mc.displayGuiScreen(new GuiConfigureOptions(event.getGui()));
        }
    }

}
