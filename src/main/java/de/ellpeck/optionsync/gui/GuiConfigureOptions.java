package de.ellpeck.optionsync.gui;

import com.google.common.io.Files;
import de.ellpeck.optionsync.OptionSync;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.util.ChatAllowedCharacters;

import java.io.File;
import java.io.IOException;

public class GuiConfigureOptions extends GuiScreen{

    private static final String[] DISALLOWED_FILENAMES = new String[]{"CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};

    private GuiListOptions options;

    private GuiButton buttonBack;
    private GuiScreen guiBefore;

    private GuiTextField saveNameField;
    private String saveName;
    private GuiButton buttonSave;

    private GuiButton buttonLoad;
    private GuiButton buttonDelete;

    public GuiConfigureOptions(GuiScreen guiBefore){
        this.guiBefore = guiBefore;
    }

    @Override
    public void initGui(){
        super.initGui();
        this.options = new GuiListOptions(this, this.mc, this.width, this.height, 32, this.height-64, 36);

        String strg = I18n.format(OptionSync.MOD_ID+".back");
        this.buttonBack = new GuiButton(0, 5, this.height-25, this.fontRendererObj.getStringWidth(strg)+15, 20, strg);
        this.buttonList.add(this.buttonBack);

        this.saveNameField = new GuiTextField(1, this.fontRendererObj, this.width-205, this.height-25, 200, 20);
        this.saveNameField.setMaxStringLength(20);

        strg = I18n.format(OptionSync.MOD_ID+".save");
        int saveWidth = this.fontRendererObj.getStringWidth(strg)+15;
        this.buttonSave = new GuiButton(2, this.width-210-saveWidth, this.height-25, saveWidth, 20, strg);
        this.buttonList.add(this.buttonSave);

        strg = I18n.format(OptionSync.MOD_ID+".delete");
        int deleteWidth = this.fontRendererObj.getStringWidth(strg)+15;
        this.buttonDelete = new GuiButton(4, this.width-5-deleteWidth, this.height-50, deleteWidth, 20, strg);
        this.buttonList.add(this.buttonDelete);

        strg = I18n.format(OptionSync.MOD_ID+".load");
        int loadWidth = this.fontRendererObj.getStringWidth(strg)+15;
        this.buttonLoad = new GuiButton(3, this.width-loadWidth-10-deleteWidth, this.height-50, loadWidth, 20, strg);
        this.buttonList.add(this.buttonLoad);

        this.updateButtonStates();
    }

    @Override
    public void updateScreen(){
        super.updateScreen();
        this.saveNameField.updateCursorCounter();

        this.updateButtonStates();
    }

    private void updateButtonStates(){
        this.buttonSave.enabled = this.saveName != null && !this.saveName.isEmpty();

        boolean selected = this.options.getCurrSelected() != null;
        this.buttonDelete.enabled = selected;
        this.buttonLoad.enabled = selected;
    }

    private void calcSaveName(){
        this.saveName = this.saveNameField.getText().trim();
        this.saveName = this.saveName.replaceAll("[\\./\"]", "_");
        for(char c : ChatAllowedCharacters.ILLEGAL_FILE_CHARACTERS){
            this.saveName = this.saveName.replace(c, '_');
        }
        for(String s : DISALLOWED_FILENAMES){
            if(this.saveName.equalsIgnoreCase(s)){
                this.saveName = "_"+this.saveName+"_";
            }
        }

        File[] files = GuiListOptions.getAllFiles();
        while(this.hasFileAlready(files, this.saveName)){
            this.saveName += "-";
        }
    }

    private boolean hasFileAlready(File[] files, String name){
        for(File file : files){
            if(name.equalsIgnoreCase(getNameWithoutFormat(file))){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(this.saveNameField.textboxKeyTyped(typedChar, keyCode)){
            this.calcSaveName();
        }
        else{
            super.keyTyped(typedChar, keyCode);
        }
    }

    public static String getNameWithoutFormat(File file){
        return file.getName().replaceAll(OptionSync.OPTION_SAVE_FORMAT, "");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        if(button == this.buttonBack){
            this.mc.displayGuiScreen(this.guiBefore);
        }
        else if(button == this.buttonSave){
            if(this.saveName != null && !this.saveName.isEmpty()){
                this.mc.gameSettings.saveOptions();

                File folder = OptionSync.getOptionsDir();
                File currentOptions = new File(this.mc.mcDataDir, "options.txt");
                File newOptions = new File(folder, this.saveName+OptionSync.OPTION_SAVE_FORMAT);

                try{
                    Files.copy(currentOptions, newOptions);
                }
                catch(IOException e){
                    OptionSync.LOGGER.error("Could not save current options!", e);
                }

                this.saveNameField.setText("");
                this.calcSaveName();

                this.options.initList();
            }
        }
        else if(button == this.buttonLoad){
            GuiListOptionsEntry entry = this.options.getCurrSelected();
            if(entry != null){
                File currentOptions = new File(this.mc.mcDataDir, "options.txt");
                File newOptions = entry.file;

                try{
                    Files.copy(newOptions, currentOptions);
                }
                catch(IOException e){
                    OptionSync.LOGGER.error("Could not load options!", e);
                }

                this.mc.gameSettings.loadOptions();

                //Fix language because this game is hideous
                if(this.mc.gameSettings.language != null){
                    LanguageManager manager = this.mc.getLanguageManager();
                    for(Language lang : manager.getLanguages()){
                        if(this.mc.gameSettings.language.equals(lang.getLanguageCode())){
                            manager.setCurrentLanguage(lang);
                        }
                    }
                }

                this.mc.refreshResources();
            }
        }
        else if(button == this.buttonDelete){
            GuiListOptionsEntry entry = this.options.getCurrSelected();
            if(entry != null){
                File file = entry.file;
                file.delete();

                this.options.setCurrSelected(null);
                this.options.initList();
            }
        }
        else{
            super.actionPerformed(button);
        }
    }

    @Override
    public void handleMouseInput() throws IOException{
        super.handleMouseInput();
        this.options.handleMouseInput();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.options.drawScreen(mouseX, mouseY, partialTicks);
        this.saveNameField.drawTextBox();

        this.drawCenteredString(this.fontRendererObj, OptionSync.NAME, this.width/2, 10, 16777215);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.saveNameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.options.mouseClicked(mouseX, mouseY, mouseButton);

        if(this.saveNameField.isFocused()){
            this.options.setCurrSelected(null);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state){
        super.mouseReleased(mouseX, mouseY, state);
        this.options.mouseReleased(mouseX, mouseY, state);
    }
}
