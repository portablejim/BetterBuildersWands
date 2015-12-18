package portablejim.bbw.core.conversion;

import net.minecraft.block.Block;

import java.util.ArrayList;

/**
 * Created by james on 18/12/15.
 */
public class CustomMappingManager {
    ArrayList<CustomMapping> mappings;

    public CustomMappingManager() {
        mappings = new ArrayList<CustomMapping>();
    }

    public CustomMapping getMapping(Block block, int meta) {
        for(CustomMapping mapping : mappings) {
            if(mapping.getLookBlock() == block && mapping.getMeta() == meta) {
                return mapping;
            }
        }
        return null;
    }

    public void setMapping(CustomMapping newMapping) {
        for(int i = 0; i < mappings.size(); i++) {
            CustomMapping current = mappings.get(i);
            if(current.getLookBlock() == newMapping.getLookBlock() && current.getMeta() == newMapping.getMeta()) {
                if(current.equals(newMapping)) {
                    // Already there
                    return;
                }
                else {
                    mappings.set(i, newMapping);
                    return;
                }
            }
        }
        mappings.add(newMapping);
    }
}
