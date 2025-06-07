package fr.myriapod.milkywayexplorer.tools;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PasteSchem {

    private final String path = "plugins/Planet/schematics/";

    public void generate(Location loc, String filename) {
        generate(loc, filename, false);
    }

    public void generate(Location loc, String fileName, boolean keepAir) {
        if(fileName == null) {
            return;
        }

        com.sk89q.worldedit.world.World weWorld = FaweAPI.getWorld(loc.getWorld().getName());
        File file = new File(path + fileName + ".schem");

        Clipboard clipboard;

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .ignoreAirBlocks(keepAir)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }


    }

}