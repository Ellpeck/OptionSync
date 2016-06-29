package de.ellpeck.optionsync;

import de.ellpeck.optionsync.gui.GuiConfigureOptions;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandling{

    //Yes, this is my birthday. I really did that.
    private static final int BUTTON_ID = -21051999;

    @SubscribeEvent
    public void onGuiOpened(GuiScreenEvent.InitGuiEvent event){
        if(event.gui instanceof GuiOptions){
            String strg = OptionSync.NAME;
            event.buttonList.add(new GuiButton(BUTTON_ID, 5, event.gui.height-25, event.gui.mc.fontRendererObj.getStringWidth(strg)+15, 20, strg));
        }
    }

    @SubscribeEvent
    public void onGuiClicked(GuiScreenEvent.ActionPerformedEvent event){
        if(event.gui instanceof GuiOptions && event.button != null && event.button.id == BUTTON_ID){
            event.gui.mc.displayGuiScreen(new GuiConfigureOptions(event.gui));
        }
    }

}
