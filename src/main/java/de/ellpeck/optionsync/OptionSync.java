package de.ellpeck.optionsync;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = OptionSync.MOD_ID, name = OptionSync.NAME, version = OptionSync.VERSION, clientSideOnly = true)
public class OptionSync{

    public static final String MOD_ID = "optionsync";
    public static final String NAME = "OptionSync";
    public static final String VERSION = "@VERSION@";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final String OPTION_SAVE_FORMAT = ".txt";

    public static File getOptionsDir(){
        String folder = System.getProperty("user.home");
        String os = System.getProperty("os.name");

        if(os.startsWith("Windows")){
            folder += "\\AppData\\Roaming\\.minecraft\\";
        }
        else if(os.startsWith("Mac")){
            folder += "/Library/Application Support/minecraft/";
        }
        else{
            folder += "/.minecraft/";
        }

        File dir = new File(folder+NAME);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        if(event.getSide() != Side.CLIENT){
            String message = NAME+" is a client-side only mod! It is not supposed to be installed on a server!";
            LOGGER.fatal(message);
            throw new RuntimeException(message);
        }

        MinecraftForge.EVENT_BUS.register(new EventHandling());
    }
}
