package de.ellpeck.optionsync.gui;

import de.ellpeck.optionsync.OptionSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GuiListOptions extends GuiListExtended{

    public final GuiConfigureOptions gui;
    private final List<GuiListOptionsEntry> options = new ArrayList<GuiListOptionsEntry>();
    private int currSelected = -1;

    public GuiListOptions(GuiConfigureOptions gui, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn){
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.gui = gui;

        this.initList();
    }

    public static File[] getAllFiles(){
        File folder = OptionSync.getOptionsDir();
        return folder.listFiles();
    }

    public void setCurrSelected(GuiListOptionsEntry entry){
        if(entry != null){
            this.currSelected = this.options.indexOf(entry);
        }
        else{
            this.currSelected = -1;
        }
    }

    public GuiListOptionsEntry getCurrSelected(){
        if(this.currSelected >= 0 && this.options.size() > this.currSelected){
            return this.options.get(this.currSelected);
        }
        else{
            this.currSelected = -1;
            return null;
        }
    }

    public void initList(){
        this.options.clear();

        File[] files = getAllFiles();
        if(files != null){
            for(File file : files){
                if(file != null && file.exists()){
                    this.options.add(new GuiListOptionsEntry(this, file));
                }
            }
        }
    }

    @Override
    protected boolean isSelected(int slotIndex){
        return slotIndex == this.currSelected;
    }

    @Override
    public IGuiListEntry getListEntry(int index){
        return this.options.get(index);
    }

    @Override
    protected int getSize(){
        return this.options.size();
    }
}
