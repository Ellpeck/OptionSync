package de.ellpeck.optionsync.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import java.io.File;

public class GuiListOptionsEntry implements GuiListExtended.IGuiListEntry{

    private final Minecraft mc;
    private final GuiListOptions list;
    public final File file;

    public GuiListOptionsEntry(GuiListOptions list, File file){
        this.list = list;
        this.file = file;
        this.mc = this.list.gui.mc;
    }

    @Override
    public void setSelected(int par1, int par2, int par3){

    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected){
        this.mc.fontRendererObj.drawString(GuiConfigureOptions.getNameWithoutFormat(this.file), x+5, y+5, 16777215);
    }

    @Override
    public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY){
        if(this.list.getCurrSelected() != this){
            this.list.setCurrSelected(this);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY){

    }
}
