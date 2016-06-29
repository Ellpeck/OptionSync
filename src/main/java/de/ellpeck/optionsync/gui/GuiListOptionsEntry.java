package de.ellpeck.optionsync.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

import java.io.File;

public class GuiListOptionsEntry implements GuiListExtended.IGuiListEntry{

    public final File file;
    private final Minecraft mc;
    private final GuiListOptions list;

    public GuiListOptionsEntry(GuiListOptions list, File file){
        this.list = list;
        this.file = file;
        this.mc = this.list.gui.mc;
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessy, int mouseX, int mouseY, boolean isSelected){
        this.mc.fontRenderer.drawString(GuiConfigureOptions.getNameWithoutFormat(this.file), x+5, y+5, 16777215);
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
